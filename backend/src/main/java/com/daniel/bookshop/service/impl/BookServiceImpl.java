package com.daniel.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daniel.bookshop.dao.BookMapper;
import com.daniel.bookshop.entity.Book;
import com.daniel.bookshop.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Override
    public List<Book> getBooksByCategory(Integer categoryId) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", categoryId);
        return list(wrapper);
    }

    @Override
    public List<Book> getBooksByUser(Integer userId) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", userId);
        return list(wrapper);
    }

    @Override
    public Book getBookDetail(Integer id) {
        return getById(id);
    }

    @Override
    public boolean addBook(Book book) {
        return save(book);
    }

    @Override
    public boolean updateBook(Book book) {
        return updateById(book);
    }

    @Override
    public boolean deleteBook(Integer id) {
        return removeById(id);
    }
} 