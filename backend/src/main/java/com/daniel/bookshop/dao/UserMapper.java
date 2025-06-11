package com.daniel.bookshop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daniel.bookshop.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
} 