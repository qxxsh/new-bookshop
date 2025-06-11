package com.daniel.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daniel.bookshop.common.OrderStatus;
import com.daniel.bookshop.dao.OrderMapper;
import com.daniel.bookshop.dto.CreateOrderDTO;
import com.daniel.bookshop.entity.Book;
import com.daniel.bookshop.entity.Cart;
import com.daniel.bookshop.entity.Order;
import com.daniel.bookshop.entity.OrderItem;
import com.daniel.bookshop.service.BookService;
import com.daniel.bookshop.service.CartService;
import com.daniel.bookshop.service.OrderItemService;
import com.daniel.bookshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private OrderItemService orderItemService;
    
    @Autowired
    private BookService bookService;
    
    // 订单号生成计数器
    private static final AtomicLong ORDER_COUNTER = new AtomicLong(1);
    
    @Override
    @Transactional
    public Order createOrderFromCart(Integer userId, CreateOrderDTO createOrderDTO) {
        // 1. 获取购物车中的选中商品
        List<Cart> cartItems = cartService.getCartList(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空，无法创建订单");
        }
        
        // 过滤出指定的购物车项
        List<Cart> selectedCartItems = cartItems.stream()
                .filter(cart -> createOrderDTO.getCartIds().contains(cart.getId()))
                .collect(Collectors.toList());
        
        if (selectedCartItems.isEmpty()) {
            throw new RuntimeException("未选择任何商品");
        }

        // 2. 检查商品是否存在（暂时跳过库存检查，因为数据库表中没有stock字段）
        for (Cart cart : selectedCartItems) {
            if (cart.getBook() != null) {
                Book book = bookService.getById(cart.getBookId());
                if (book == null) {
                    throw new RuntimeException("商品不存在：" + cart.getBook().getName());
                }
                // 库存检查已移除，因为数据库表中没有stock字段
            }
        }
        
        // 3. 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart cart : selectedCartItems) {
            if (cart.getBook() != null) {
                BigDecimal itemTotal = cart.getBook().getPrice()
                        .multiply(new BigDecimal(cart.getQuantity()));
                totalAmount = totalAmount.add(itemTotal);
            }
        }
        
        // 4. 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING_PAYMENT.getCode());
        order.setDeliveryAddress(createOrderDTO.getDeliveryAddress());
        order.setDeliveryPhone(createOrderDTO.getDeliveryPhone());
        order.setDeliveryName(createOrderDTO.getDeliveryName());
        order.setRemark(createOrderDTO.getRemark());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        // 保存订单
        if (!save(order)) {
            throw new RuntimeException("创建订单失败");
        }
        
        // 5. 创建订单详情
        for (Cart cart : selectedCartItems) {
            if (cart.getBook() != null) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setBookId(cart.getBookId());
                orderItem.setBookName(cart.getBook().getName());
                orderItem.setBookAuthor(cart.getBook().getAuthor());
                orderItem.setBookPrice(cart.getBook().getPrice());
                orderItem.setQuantity(cart.getQuantity());
                orderItem.setSubtotal(cart.getBook().getPrice()
                        .multiply(new BigDecimal(cart.getQuantity())));
                orderItem.setCreateTime(LocalDateTime.now());
                
                orderItemService.save(orderItem);
            }
        }
        
        // 6. 清空购物车中的已下单商品
        for (Cart cart : selectedCartItems) {
            cartService.removeFromCart(cart.getId(), userId);
        }
        
        return order;
    }
    
    @Override
    public Page<Order> getUserOrders(Integer userId, Integer pageNum, Integer pageSize, Integer status) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.orderByDesc("create_time");
        
        Page<Order> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        
        // 为每个订单设置订单项
        for (Order order : page.getRecords()) {
            List<OrderItem> orderItems = orderItemService.getOrderItems(order.getId());
            order.setOrderItems(orderItems);
        }
        
        return page;
    }
    
    @Override
    public Order getOrderDetail(Integer orderId, Integer userId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId).eq("user_id", userId);
        Order order = getOne(queryWrapper);
        
        if (order != null) {
            // 获取订单项
            List<OrderItem> orderItems = orderItemService.getOrderItems(orderId);
            order.setOrderItems(orderItems);
        }
        
        return order;
    }
    
    @Override
    @Transactional
    public boolean cancelOrder(Integer orderId, Integer userId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId).eq("user_id", userId);
        Order order = getOne(queryWrapper);
        
        if (order == null) {
            return false;
        }
        
        // 只有待支付状态的订单才能取消
        if (!OrderStatus.PENDING_PAYMENT.getCode().equals(order.getStatus())) {
            return false;
        }
        
        // 库存恢复功能已移除，因为数据库表中没有stock字段
        
        order.setStatus(OrderStatus.CANCELLED.getCode());
        order.setUpdateTime(LocalDateTime.now());
        
        return updateById(order);
    }
    
    @Override
    public boolean confirmOrder(Integer orderId, Integer userId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId).eq("user_id", userId);
        Order order = getOne(queryWrapper);
        
        if (order == null) {
            return false;
        }
        
        // 只有已发货状态的订单才能确认收货
        if (!OrderStatus.SHIPPED.getCode().equals(order.getStatus())) {
            return false;
        }
        
        order.setStatus(OrderStatus.COMPLETED.getCode());
        order.setUpdateTime(LocalDateTime.now());
        
        return updateById(order);
    }
    
    @Override
    public boolean payOrder(Integer orderId, Integer userId, String paymentMethod) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId).eq("user_id", userId);
        Order order = getOne(queryWrapper);
        
        if (order == null) {
            return false;
        }
        
        // 只有待支付状态的订单才能支付
        if (!OrderStatus.PENDING_PAYMENT.getCode().equals(order.getStatus())) {
            return false;
        }
        
        order.setStatus(OrderStatus.PAID.getCode());
        order.setPaymentMethod(paymentMethod);
        order.setPaymentTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        return updateById(order);
    }
    
    @Override
    public String generateOrderNo() {
        // 格式：年月日时分秒 + 6位递增序号
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        long counter = ORDER_COUNTER.getAndIncrement() % 1000000; // 6位数字循环
        return timestamp + String.format("%06d", counter);
    }
} 