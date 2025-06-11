package com.daniel.bookshop.controller;

import com.daniel.bookshop.common.Response;
import com.daniel.bookshop.entity.BookImage;
import com.daniel.bookshop.service.BookImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookImage")
public class BookImageController {
    @Autowired
    private BookImageService bookImageService;

    @GetMapping("/book/{bookId}")
    public List<BookImage> getImagesByBookId(@PathVariable Integer bookId) {
        return bookImageService.getImagesByBookId(bookId);
    }

    @PostMapping
    public Response<BookImage> addImage(@RequestBody BookImage image) {
        System.out.println("=== 图片关联调试 ===");
        System.out.println("接收到的图片关联请求:");
        System.out.println("  书籍ID (bid): " + image.getBid());
        System.out.println("  图片类型 (type): " + image.getType());
        
        // 确保type字段有值，如果为null则设置为0
        if (image.getType() == null) {
            image.setType(0);
            System.out.println("  type字段为null，设置为默认值0");
        }
        
        boolean result = bookImageService.addImage(image);
        System.out.println("图片关联结果: " + result);
        
        if (result) {
            System.out.println("图片记录ID: " + image.getId());
            return Response.success(image);
        } else {
            System.out.println("图片关联失败");
            return Response.error("图片关联失败");
        }
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteImage(@PathVariable Integer id) {
        boolean result = bookImageService.deleteImage(id);
        return result ? Response.success() : Response.error("删除失败");
    }
} 