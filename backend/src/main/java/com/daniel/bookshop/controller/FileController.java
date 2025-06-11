package com.daniel.bookshop.controller;

import com.daniel.bookshop.common.Result;
import com.daniel.bookshop.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = FileUtil.upload(file, uploadPath);
            return Result.success(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件上传失败");
        }
    }
    
    @PostMapping("/upload-book-image")
    public Result<String> uploadBookImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("imageId") Integer imageId) {
        try {
            // 检查文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$")) {
                return Result.error("只支持图片文件（jpg、jpeg、png、gif）");
            }
            
            // 使用BookImage的ID作为文件名
            String fileName = imageId + ".jpg";
            
            // 获取项目根目录（更可靠的方法）
            String userDir = System.getProperty("user.dir");
            System.out.println("当前工作目录: " + userDir);
            
            // 如果当前目录是backend，需要回到父目录
            if (userDir.endsWith("backend")) {
                userDir = new File(userDir).getParent();
            }
            
            String frontendImagePath = "frontend" + File.separator + "public" + File.separator + 
                                     "img" + File.separator + "book-list" + File.separator + "article";
            String fullPath = userDir + File.separator + frontendImagePath;
            
            System.out.println("项目根目录: " + userDir);
            System.out.println("上传图片到路径: " + fullPath);
            System.out.println("文件名: " + fileName);
            
            // 确保目录存在
            File uploadDir = new File(fullPath);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                System.out.println("创建目录结果: " + created);
            }
            
            String result = FileUtil.uploadWithName(file, fullPath, fileName);
            
            // 验证文件是否真正保存成功
            File savedFile = new File(fullPath + File.separator + fileName);
            if (savedFile.exists()) {
                System.out.println("文件保存成功，大小: " + savedFile.length() + " bytes");
            } else {
                System.out.println("警告：文件保存失败，文件不存在");
            }
            
            return Result.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("图书封面上传失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{fileName}")
    public Result<Void> delete(@PathVariable String fileName) {
        String filePath = uploadPath + File.separator + fileName;
        boolean success = FileUtil.delete(filePath);
        return success ? Result.success() : Result.error("文件删除失败");
    }
} 