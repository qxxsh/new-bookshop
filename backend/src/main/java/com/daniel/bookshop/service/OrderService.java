package com.daniel.bookshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daniel.bookshop.dto.CreateOrderDTO;
import com.daniel.bookshop.entity.Order;

public interface OrderService extends IService<Order> {
    
    /**
     * 从购物车创建订单
     */
    Order createOrderFromCart(Integer userId, CreateOrderDTO createOrderDTO);
    
    /**
     * 获取用户订单列表（分页）
     */
    Page<Order> getUserOrders(Integer userId, Integer pageNum, Integer pageSize, Integer status);
    
    /**
     * 获取订单详情（包含订单项）
     */
    Order getOrderDetail(Integer orderId, Integer userId);
    
    /**
     * 取消订单
     */
    boolean cancelOrder(Integer orderId, Integer userId);
    
    /**
     * 确认收货
     */
    boolean confirmOrder(Integer orderId, Integer userId);
    
    /**
     * 支付订单（模拟支付）
     */
    boolean payOrder(Integer orderId, Integer userId, String paymentMethod);
    
    /**
     * 生成订单号
     */
    String generateOrderNo();
} 