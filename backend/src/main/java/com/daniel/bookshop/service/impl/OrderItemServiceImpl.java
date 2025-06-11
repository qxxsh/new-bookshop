package com.daniel.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daniel.bookshop.dao.OrderItemMapper;
import com.daniel.bookshop.entity.OrderItem;
import com.daniel.bookshop.service.BookService;
import com.daniel.bookshop.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {
    
    @Autowired
    private BookService bookService;
    
    @Override
    public List<OrderItem> getOrderItems(Integer orderId) {
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        List<OrderItem> orderItems = list(queryWrapper);
        
        // 为每个订单项设置书籍信息
        for (OrderItem orderItem : orderItems) {
            orderItem.setBook(bookService.getById(orderItem.getBookId()));
        }
        
        return orderItems;
    }
} 