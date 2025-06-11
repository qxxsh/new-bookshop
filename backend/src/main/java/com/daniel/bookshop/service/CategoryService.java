package com.daniel.bookshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daniel.bookshop.entity.Category;
import java.util.List;

public interface CategoryService extends IService<Category> {
    List<Category> getAllCategories();
    Category getCategoryById(Integer id);
    boolean addCategory(Category category);
    boolean updateCategory(Category category);
    boolean deleteCategory(Integer id);
} 