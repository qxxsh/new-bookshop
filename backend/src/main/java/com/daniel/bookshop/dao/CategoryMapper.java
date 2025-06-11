package com.daniel.bookshop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daniel.bookshop.entity.Category;
import org.apache.ibatis.annotations.Mapper;
 
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
} 