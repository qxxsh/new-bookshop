package com.daniel.bookshop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daniel.bookshop.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
} 