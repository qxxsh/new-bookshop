package com.daniel.bookshop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daniel.bookshop.common.Response;
import com.daniel.bookshop.entity.Book;
import com.daniel.bookshop.entity.BookImage;
import com.daniel.bookshop.service.BookService;
import com.daniel.bookshop.service.BookImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookImageService bookImageService;

    @GetMapping("/list")
    public Response<Page<Book>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer bookType,
            @RequestParam(required = false) Integer userId) {
        
        System.out.println("=== Book列表查询调试信息 ===");
        System.out.println("pageNum: " + pageNum);
        System.out.println("pageSize: " + pageSize);
        System.out.println("keyword: " + keyword);
        System.out.println("categoryId: " + categoryId);
        System.out.println("bookType: " + bookType);
        System.out.println("userId: " + userId);
        
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("name", keyword)
                    .or()
                    .like("author", keyword);
        }
        if (categoryId != null) {
            queryWrapper.eq("cid", categoryId);
        }
        if (bookType != null) {
            queryWrapper.eq("bookType", bookType);
            System.out.println("添加bookType过滤条件: " + bookType);
        }
        if (userId != null) {
            queryWrapper.eq("uid", userId);
            System.out.println("添加userId过滤条件: " + userId);
        }
        
        // 打印最终的SQL查询条件
        System.out.println("查询条件: " + queryWrapper.getSqlSegment());
        
        Page<Book> page = bookService.page(new Page<>(pageNum, pageSize), queryWrapper);
        
        System.out.println("查询结果总数: " + page.getTotal());
        System.out.println("当前页记录数: " + page.getRecords().size());
        
        // 为每本书加载关联的BookImage数据
        for (Book book : page.getRecords()) {
            loadBookImage(book);
            System.out.println("书籍: " + book.getName() + ", 类型: " + book.getBookType() + ", 用户ID: " + book.getUid());
        }
        
        return Response.success(page);
    }

    @GetMapping("/{id}")
    public Response<Book> getById(@PathVariable Integer id) {
        Book book = bookService.getById(id);
        if (book != null) {
            loadBookImage(book);
            return Response.success(book);
        }
        return Response.error("图书不存在");
    }
    
    /**
     * 为图书加载关联的BookImage数据
     */
    private void loadBookImage(Book book) {
        try {
            QueryWrapper<BookImage> imageWrapper = new QueryWrapper<>();
            imageWrapper.eq("bid", book.getId())
                       .eq("type", 0); // 只查询type=0的图片记录
            BookImage bookImage = bookImageService.getOne(imageWrapper);
            if (bookImage != null) {
                book.setBookImage(bookImage);
            } else {
                // 如果没有找到图像，尝试查询没有type限制的记录
                QueryWrapper<BookImage> fallbackWrapper = new QueryWrapper<>();
                fallbackWrapper.eq("bid", book.getId());
                BookImage fallbackImage = bookImageService.getOne(fallbackWrapper);
                
                if (fallbackImage != null) {
                    book.setBookImage(fallbackImage);
                } else {
                    // 如果还是没有找到图像，设置默认值
                    BookImage defaultImage = new BookImage();
                    defaultImage.setId(1); // 使用1作为默认图像ID，对应1.jpg
                    book.setBookImage(defaultImage);
                }
            }
        } catch (Exception e) {
            // 如果出现异常，设置默认值
            BookImage defaultImage = new BookImage();
            defaultImage.setId(1); // 使用1作为默认图像ID，对应1.jpg
            book.setBookImage(defaultImage);
        }
    }

    @PostMapping
    public Response<Book> add(@RequestBody Book book) {
        try {
            // 验证degree字段范围
            if (book.getDegree() != null && book.getDegree().compareTo(new java.math.BigDecimal("9.9")) > 0) {
                return Response.error("品相值不能超过9.9");
            }
            
            // 设置创建时间
            book.setDate(java.time.LocalDateTime.now());
            
            boolean success = bookService.save(book);
            return success ? Response.success(book) : Response.error("添加失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("添加失败: " + e.getMessage());
        }
    }

    @PutMapping
    public Response<Book> update(@RequestBody Book book) {
        try {
            // 验证degree字段范围
            if (book.getDegree() != null && book.getDegree().compareTo(new java.math.BigDecimal("9.9")) > 0) {
                return Response.error("品相值不能超过9.9");
            }
            
            boolean success = bookService.updateById(book);
            return success ? Response.success(book) : Response.error("更新失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Integer id) {
        boolean success = bookService.removeById(id);
        return success ? Response.success() : Response.error("删除失败");
    }
    
    // 调试接口：查看数据详情
    @GetMapping("/debug/all")
    public Response<Object> debugAll() {
        try {
            // 获取所有图书
            List<Book> books = bookService.list();
            System.out.println("数据库中的图书总数: " + books.size());
            
            // 获取所有图片
            List<BookImage> images = bookImageService.list();
            System.out.println("数据库中的图片总数: " + images.size());
            
            // 为前5本书加载图片信息
            List<Book> debugBooks = books.stream().limit(5).collect(java.util.stream.Collectors.toList());
            for (Book book : debugBooks) {
                loadBookImage(book);
                System.out.println("图书: " + book.getName() + ", 图片ID: " + 
                    (book.getBookImage() != null ? book.getBookImage().getId() : "null"));
            }
            
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("totalBooks", books.size());
            result.put("totalImages", images.size());
            result.put("sampleBooks", debugBooks);
            result.put("allImages", images);
            
            return Response.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("调试失败: " + e.getMessage());
        }
    }
    
    // 快速测试数据导入接口
    @PostMapping("/debug/init-test-data")
    public Response<String> initTestData() {
        try {
            // 检查是否已有数据
            long count = bookService.count();
            if (count > 0) {
                return Response.error("数据库中已有 " + count + " 本图书，请先清空或使用完整SQL导入");
            }
            
            // 创建几本测试图书
            Book book1 = new Book();
            book1.setName("Java编程思想");
            book1.setCid(7);
            book1.setBookType(1);
            book1.setPrice(new java.math.BigDecimal("52.00"));
            book1.setOriginalPrice(new java.math.BigDecimal("108.00"));
            book1.setUid(1);
            book1.setAuthor("Bruce Eckel");
            book1.setPress("机械工业出版社");
            book1.setVersion("第4版");
            book1.setDegree(new java.math.BigDecimal("9.5"));
            book1.setPublishDate("2016-11");
            book1.setDescription("四大名著啊！能不看吗");
            book1.setDate(java.time.LocalDateTime.now());
            
            Book book2 = new Book();
            book2.setName("明朝那些事儿（全9册）");
            book2.setCid(2);
            book2.setBookType(1);
            book2.setPrice(new java.math.BigDecimal("99.00"));
            book2.setOriginalPrice(new java.math.BigDecimal("178.00"));
            book2.setUid(1);
            book2.setAuthor("当年明月");
            book2.setPress("北京联合出版公司");
            book2.setVersion("平装");
            book2.setDegree(new java.math.BigDecimal("8.5"));
            book2.setPublishDate("2017-05");
            book2.setDescription("没买多久，急用钱，低价甩");
            book2.setDate(java.time.LocalDateTime.now());
            
            Book book3 = new Book();
            book3.setName("恶意");
            book3.setCid(1);
            book3.setBookType(1);
            book3.setPrice(new java.math.BigDecimal("9.90"));
            book3.setOriginalPrice(new java.math.BigDecimal("28.00"));
            book3.setUid(1);
            book3.setAuthor("东野圭吾");
            book3.setPress("南海出版公司");
            book3.setVersion("平装");
            book3.setDegree(new java.math.BigDecimal("9.0"));
            book3.setPublishDate("2008-09");
            book3.setDescription("书架太多书了 出给东野圭吾书迷");
            book3.setDate(java.time.LocalDateTime.now());
            
            // 保存图书
            bookService.save(book1);
            bookService.save(book2);
            bookService.save(book3);
            
            // 创建对应的图片关联
            BookImage img1 = new BookImage();
            img1.setBid(book1.getId());
            img1.setType(0);
            bookImageService.save(img1);
            
            BookImage img2 = new BookImage();
            img2.setBid(book2.getId());
            img2.setType(0);
            bookImageService.save(img2);
            
            BookImage img3 = new BookImage();
            img3.setBid(book3.getId());
            img3.setType(0);
            bookImageService.save(img3);
            
            return Response.success("成功创建3本测试图书数据！请刷新页面查看效果。");
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("创建测试数据失败: " + e.getMessage());
        }
    }
} 