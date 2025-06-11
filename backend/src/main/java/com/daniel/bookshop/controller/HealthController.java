package com.daniel.bookshop.controller;

import com.daniel.bookshop.common.Response;
import com.daniel.bookshop.service.BookService;
import com.daniel.bookshop.service.BookImageService;
import com.daniel.bookshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookImageService bookImageService;
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/check")
    public Response<Map<String, Object>> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查数据库连接和数据
            long bookCount = bookService.count();
            long imageCount = bookImageService.count();
            long categoryCount = categoryService.count();
            
            result.put("database_status", "connected");
            result.put("book_count", bookCount);
            result.put("image_count", imageCount);
            result.put("category_count", categoryCount);
            
            // 数据完整性检查
            if (bookCount == 0) {
                result.put("warning", "数据库中没有图书数据，请导入 docs/sql/bookshop.sql");
            } else if (imageCount == 0) {
                result.put("warning", "数据库中没有图片数据，图书将显示默认图片");
            } else {
                result.put("status", "系统正常");
            }
            
            // 预期数据量
            result.put("expected_books", 88);
            result.put("expected_images", 84);
            result.put("expected_categories", 9);
            
            // 数据是否完整
            result.put("data_complete", bookCount >= 80 && imageCount >= 80 && categoryCount >= 9);
            
            return Response.success(result);
            
        } catch (Exception e) {
            result.put("database_status", "error");
            result.put("error_message", e.getMessage());
            result.put("suggestion", "请检查MySQL服务是否启动，数据库是否存在");
            return Response.error("数据库连接失败: " + e.getMessage());
        }
    }
} 