package com.daniel.bookshop.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {
    public static String upload(MultipartFile file, String uploadPath) throws IOException {
        // 创建上传目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;

        // 保存文件
        File dest = new File(uploadPath + File.separator + fileName);
        file.transferTo(dest);

        return fileName;
    }

    public static String uploadWithName(MultipartFile file, String uploadPath, String fileName) throws IOException {
        // 创建上传目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 保存文件
        File dest = new File(uploadPath + File.separator + fileName);
        file.transferTo(dest);

        return fileName;
    }

    public static boolean delete(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
} 