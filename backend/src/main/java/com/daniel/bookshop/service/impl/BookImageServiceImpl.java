package com.daniel.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daniel.bookshop.dao.BookImageMapper;
import com.daniel.bookshop.entity.BookImage;
import com.daniel.bookshop.service.BookImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookImageServiceImpl extends ServiceImpl<BookImageMapper, BookImage> implements BookImageService {
    @Override
    public List<BookImage> getImagesByBookId(Integer bookId) {
        QueryWrapper<BookImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bid", bookId);
        return list(queryWrapper);
    }

    @Override
    public boolean addImage(BookImage image) {
        return save(image);
    }

    @Override
    public boolean deleteImage(Integer id) {
        return removeById(id);
    }
} 