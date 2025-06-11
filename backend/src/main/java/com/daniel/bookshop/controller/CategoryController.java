package com.daniel.bookshop.controller;

import com.daniel.bookshop.common.Response;
import com.daniel.bookshop.entity.Category;
import com.daniel.bookshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Response<List<Category>> list() {
        List<Category> categories = categoryService.list();
        return Response.success(categories);
    }

    @GetMapping("/{id}")
    public Response<Category> getById(@PathVariable Integer id) {
        Category category = categoryService.getById(id);
        return category != null ? Response.success(category) : Response.error("分类不存在");
    }

    @PostMapping
    public Response<Category> add(@RequestBody Category category) {
        boolean success = categoryService.save(category);
        return success ? Response.success(category) : Response.error("添加失败");
    }

    @PutMapping
    public Response<Category> update(@RequestBody Category category) {
        boolean success = categoryService.updateById(category);
        return success ? Response.success(category) : Response.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Integer id) {
        boolean success = categoryService.removeById(id);
        return success ? Response.success() : Response.error("删除失败");
    }
} 