package com.daniel.bookshop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daniel.bookshop.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    
    /**
     * 获取用户购物车列表（使用明确的字段别名）
     */
    @Select("SELECT id, user_id AS userId, book_id AS bookId, quantity, " +
            "create_time AS createTime, update_time AS updateTime " +
            "FROM cart WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Cart> getCartWithBookInfo(Integer userId);
} 