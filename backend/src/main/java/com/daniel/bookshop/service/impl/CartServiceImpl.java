package com.daniel.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daniel.bookshop.dao.CartMapper;
import com.daniel.bookshop.entity.Book;
import com.daniel.bookshop.entity.BookImage;
import com.daniel.bookshop.entity.Cart;
import com.daniel.bookshop.service.BookService;
import com.daniel.bookshop.service.BookImageService;
import com.daniel.bookshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookImageService bookImageService;
    
    @Override
    public boolean addToCart(Integer userId, Integer bookId, Integer quantity) {
        System.out.println("CartService.addToCart - 用户ID: " + userId + ", 书籍ID: " + bookId + ", 数量: " + quantity);
        
        // 检查书籍是否存在
        Book book = bookService.getById(bookId);
        if (book == null) {
            System.out.println("书籍不存在，bookId: " + bookId);
            return false;
        }
        
        System.out.println("找到书籍: " + book.getName() + " (ID: " + book.getId() + ")");
        
        // 检查购物车中是否已存在该商品
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("book_id", bookId);
        Cart existingCart = getOne(queryWrapper);
        
        if (existingCart != null) {
            System.out.println("购物车中已存在该商品，更新数量");
            // 如果已存在，更新数量
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            existingCart.setUpdateTime(LocalDateTime.now());
            boolean result = updateById(existingCart);
            System.out.println("更新购物车结果: " + result);
            return result;
        } else {
            System.out.println("购物车中不存在该商品，新增记录");
            // 如果不存在，新增记录
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setBookId(bookId);
            cart.setQuantity(quantity);
            cart.setCreateTime(LocalDateTime.now());
            cart.setUpdateTime(LocalDateTime.now());
            boolean result = save(cart);
            System.out.println("新增购物车记录结果: " + result);
            if (result) {
                System.out.println("新增的购物车记录ID: " + cart.getId());
            }
            return result;
        }
    }
    
    @Override
    public List<Cart> getCartList(Integer userId) {
        System.out.println("获取购物车列表，用户ID: " + userId);
        
        // 使用标准的MyBatis-Plus查询，避免字段映射问题
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("create_time");
        List<Cart> cartList = list(queryWrapper);
        
        System.out.println("查询到购物车记录数: " + cartList.size());
        
        // 为每个购物车项设置完整的书籍信息
        for (Cart cart : cartList) {
            System.out.println("购物车记录 - ID: " + cart.getId() + ", 用户ID: " + cart.getUserId() + ", 书籍ID: " + cart.getBookId() + ", 数量: " + cart.getQuantity());
            
            // 获取完整的书籍信息（包括BookImage）
            Book book = bookService.getById(cart.getBookId());
            if (book != null) {
                System.out.println("  对应书籍: " + book.getName() + " (ID: " + book.getId() + ")");
                
                // 获取书籍图片信息
                try {
                    QueryWrapper<BookImage> imageWrapper = new QueryWrapper<>();
                    imageWrapper.eq("bid", book.getId()).eq("type", 0);
                    BookImage bookImage = bookImageService.getOne(imageWrapper);
                    if (bookImage != null) {
                        book.setBookImage(bookImage);
                        System.out.println("  书籍图片ID: " + bookImage.getId());
                    } else {
                        System.out.println("  书籍无type=0的图片，尝试查询其他图片");
                        // 如果没有type=0的图片，尝试查询其他图片
                        QueryWrapper<BookImage> fallbackWrapper = new QueryWrapper<>();
                        fallbackWrapper.eq("bid", book.getId());
                        BookImage fallbackImage = bookImageService.getOne(fallbackWrapper);
                        if (fallbackImage != null) {
                            book.setBookImage(fallbackImage);
                            System.out.println("  使用备选图片ID: " + fallbackImage.getId());
                        } else {
                            System.out.println("  书籍无图片，使用默认图片");
                            // 设置默认图片
                            BookImage defaultImage = new BookImage();
                            defaultImage.setId(1); // 使用1作为默认图像ID
                            book.setBookImage(defaultImage);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("  获取书籍图片失败: " + e.getMessage());
                    // 设置默认图片
                    BookImage defaultImage = new BookImage();
                    defaultImage.setId(1); // 使用1作为默认图像ID
                    book.setBookImage(defaultImage);
                }
                
                cart.setBook(book);
            } else {
                System.out.println("  警告：书籍ID " + cart.getBookId() + " 对应的书籍不存在");
            }
        }
        
        return cartList;
    }
    
    @Override
    public boolean updateQuantity(Integer cartId, Integer quantity, Integer userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", cartId).eq("user_id", userId);
        Cart cart = getOne(queryWrapper);
        
        if (cart != null) {
            cart.setQuantity(quantity);
            cart.setUpdateTime(LocalDateTime.now());
            return updateById(cart);
        }
        return false;
    }
    
    @Override
    public boolean removeFromCart(Integer cartId, Integer userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", cartId).eq("user_id", userId);
        return remove(queryWrapper);
    }
    
    @Override
    public boolean clearCart(Integer userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return remove(queryWrapper);
    }
    
    @Override
    public Integer getCartCount(Integer userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return Math.toIntExact(count(queryWrapper));
    }
} 