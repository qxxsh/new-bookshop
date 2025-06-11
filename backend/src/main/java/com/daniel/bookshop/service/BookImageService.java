package com.daniel.bookshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daniel.bookshop.entity.BookImage;
import java.util.List;
 
public interface BookImageService extends IService<BookImage> {
    List<BookImage> getImagesByBookId(Integer bookId);
    boolean addImage(BookImage image);
    boolean deleteImage(Integer id);
} 