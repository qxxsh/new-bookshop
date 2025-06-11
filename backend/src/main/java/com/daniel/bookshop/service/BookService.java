package com.daniel.bookshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daniel.bookshop.entity.Book;
import java.util.List;

public interface BookService extends IService<Book> {
    List<Book> getBooksByCategory(Integer categoryId);
    List<Book> getBooksByUser(Integer userId);
    Book getBookDetail(Integer id);
    boolean addBook(Book book);
    boolean updateBook(Book book);
    boolean deleteBook(Integer id);
} 