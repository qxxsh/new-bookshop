package com.daniel.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daniel.bookshop.common.OrderStatus;
import com.daniel.bookshop.dao.ReviewMapper;
import com.daniel.bookshop.dto.CreateReviewDTO;
import com.daniel.bookshop.entity.Order;
import com.daniel.bookshop.entity.OrderItem;
import com.daniel.bookshop.entity.Review;
import com.daniel.bookshop.service.BookService;
import com.daniel.bookshop.service.OrderItemService;
import com.daniel.bookshop.service.OrderService;
import com.daniel.bookshop.service.ReviewService;
import com.daniel.bookshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderItemService orderItemService;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private UserService userService;
    
    @Override
    @Transactional
    public Review createReview(Integer userId, CreateReviewDTO createReviewDTO) {
        // 1. 验证订单项是否存在且属于该用户
        OrderItem orderItem = orderItemService.getById(createReviewDTO.getOrderItemId());
        if (orderItem == null) {
            throw new RuntimeException("订单项不存在");
        }
        
        // 2. 验证订单是否属于该用户且已完成
        Order order = orderService.getById(orderItem.getOrderId());
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在或不属于当前用户");
        }
        
        if (!OrderStatus.COMPLETED.getCode().equals(order.getStatus())) {
            throw new RuntimeException("只有已完成的订单才能评价");
        }
        
        // 3. 检查是否已经评价过
        if (isOrderItemReviewed(createReviewDTO.getOrderItemId())) {
            throw new RuntimeException("该商品已经评价过了");
        }
        
        // 4. 验证评分范围
        if (createReviewDTO.getRating() < 1 || createReviewDTO.getRating() > 5) {
            throw new RuntimeException("评分必须在1-5星之间");
        }
        
        // 5. 创建评价
        Review review = new Review();
        review.setOrderId(order.getId());
        review.setOrderItemId(createReviewDTO.getOrderItemId());
        review.setBookId(orderItem.getBookId());
        review.setUserId(userId);
        review.setRating(createReviewDTO.getRating());
        review.setContent(createReviewDTO.getContent());
        review.setIsAnonymous(createReviewDTO.getIsAnonymous() != null ? createReviewDTO.getIsAnonymous() : false);
        review.setCreateTime(LocalDateTime.now());
        review.setUpdateTime(LocalDateTime.now());
        
        if (!save(review)) {
            throw new RuntimeException("评价创建失败");
        }
        
        return review;
    }
    
    @Override
    public Page<Review> getBookReviews(Integer bookId, Integer pageNum, Integer pageSize) {
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id", bookId);
        queryWrapper.orderByDesc("create_time");
        
        Page<Review> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        
        // 设置关联信息
        for (Review review : page.getRecords()) {
            if (!review.getIsAnonymous()) {
                review.setUser(userService.getById(review.getUserId()));
            }
            review.setBook(bookService.getById(review.getBookId()));
        }
        
        return page;
    }
    
    @Override
    public Page<Review> getUserReviews(Integer userId, Integer pageNum, Integer pageSize) {
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        
        Page<Review> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        
        // 设置关联信息
        for (Review review : page.getRecords()) {
            review.setBook(bookService.getById(review.getBookId()));
            review.setUser(userService.getById(review.getUserId()));
        }
        
        return page;
    }
    
    @Override
    public Double getBookAverageRating(Integer bookId) {
        return baseMapper.getBookAverageRating(bookId);
    }
    
    @Override
    public List<Object> getBookRatingStats(Integer bookId) {
        return baseMapper.getBookRatingStats(bookId);
    }
    
    @Override
    public boolean canReviewOrderItem(Integer userId, Integer orderItemId) {
        // 1. 检查订单项是否存在
        OrderItem orderItem = orderItemService.getById(orderItemId);
        if (orderItem == null) {
            return false;
        }
        
        // 2. 检查订单是否属于用户且已完成
        Order order = orderService.getById(orderItem.getOrderId());
        if (order == null || !order.getUserId().equals(userId)) {
            return false;
        }
        
        if (!OrderStatus.COMPLETED.getCode().equals(order.getStatus())) {
            return false;
        }
        
        // 3. 检查是否已经评价过
        return !isOrderItemReviewed(orderItemId);
    }
    
    @Override
    public boolean isOrderItemReviewed(Integer orderItemId) {
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_item_id", orderItemId);
        return count(queryWrapper) > 0;
    }
} 