package com.daniel.bookshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daniel.bookshop.dto.CreateReviewDTO;
import com.daniel.bookshop.entity.Review;

import java.util.List;

public interface ReviewService extends IService<Review> {
    
    /**
     * 创建评价
     */
    Review createReview(Integer userId, CreateReviewDTO createReviewDTO);
    
    /**
     * 获取书籍的评价列表（分页）
     */
    Page<Review> getBookReviews(Integer bookId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取用户的评价列表（分页）
     */
    Page<Review> getUserReviews(Integer userId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取书籍的平均评分
     */
    Double getBookAverageRating(Integer bookId);
    
    /**
     * 获取书籍的评价统计
     */
    List<Object> getBookRatingStats(Integer bookId);
    
    /**
     * 检查用户是否可以评价某个订单项
     */
    boolean canReviewOrderItem(Integer userId, Integer orderItemId);
    
    /**
     * 检查订单项是否已经评价
     */
    boolean isOrderItemReviewed(Integer orderItemId);
} 