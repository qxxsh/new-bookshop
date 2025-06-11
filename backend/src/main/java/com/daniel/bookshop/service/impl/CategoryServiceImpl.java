package com.daniel.bookshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daniel.bookshop.dao.CategoryMapper;
import com.daniel.bookshop.entity.Category;
import com.daniel.bookshop.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public List<Category> getAllCategories() {
        return list();
    }

    @Override
    public Category getCategoryById(Integer id) {
        return getById(id);
    }

    @Override
    public boolean addCategory(Category category) {
        return save(category);
    }

    @Override
    public boolean updateCategory(Category category) {
        return updateById(category);
    }

    @Override
    public boolean deleteCategory(Integer id) {
        return removeById(id);
    }
} 