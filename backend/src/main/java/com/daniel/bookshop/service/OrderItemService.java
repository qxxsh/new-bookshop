package com.daniel.bookshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daniel.bookshop.entity.OrderItem;

import java.util.List;

public interface OrderItemService extends IService<OrderItem> {
    
    /**
     * 获取订单的所有商品项
     */
    List<OrderItem> getOrderItems(Integer orderId);
} 