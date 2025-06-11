package com.daniel.bookshop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daniel.bookshop.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    
    /**
     * 获取用户订单列表（包含订单项数量）
     */
    @Select("SELECT o.*, COUNT(oi.id) as item_count " +
            "FROM `order` o LEFT JOIN order_item oi ON o.id = oi.order_id " +
            "WHERE o.user_id = #{userId} " +
            "GROUP BY o.id " +
            "ORDER BY o.create_time DESC")
    List<Order> getUserOrdersWithItemCount(Integer userId);
} 