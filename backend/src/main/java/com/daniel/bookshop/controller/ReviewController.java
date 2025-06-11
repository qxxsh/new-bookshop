package com.daniel.bookshop.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daniel.bookshop.common.Constants;
import com.daniel.bookshop.common.Response;
import com.daniel.bookshop.dto.CreateReviewDTO;
import com.daniel.bookshop.entity.Review;
import com.daniel.bookshop.entity.User;
import com.daniel.bookshop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    /**
     * 创建评价
     */
    @PostMapping("/create")
    public Response<Review> createReview(@RequestBody CreateReviewDTO createReviewDTO, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        try {
            Review review = reviewService.createReview(user.getId(), createReviewDTO);
            return Response.success(review);
        } catch (Exception e) {
            return Response.error("评价失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取书籍的评价列表
     */
    @GetMapping("/book/{bookId}")
    public Response<Page<Review>> getBookReviews(
            @PathVariable Integer bookId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Review> reviews = reviewService.getBookReviews(bookId, pageNum, pageSize);
        return Response.success(reviews);
    }
    
    /**
     * 获取用户的评价列表
     */
    @GetMapping("/user")
    public Response<Page<Review>> getUserReviews(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        Page<Review> reviews = reviewService.getUserReviews(user.getId(), pageNum, pageSize);
        return Response.success(reviews);
    }
    
    /**
     * 获取书籍的平均评分
     */
    @GetMapping("/book/{bookId}/rating")
    public Response<Double> getBookAverageRating(@PathVariable Integer bookId) {
        Double rating = reviewService.getBookAverageRating(bookId);
        return Response.success(rating != null ? rating : 0.0);
    }
    
    /**
     * 获取书籍的评分统计
     */
    @GetMapping("/book/{bookId}/stats")
    public Response<List<Object>> getBookRatingStats(@PathVariable Integer bookId) {
        List<Object> stats = reviewService.getBookRatingStats(bookId);
        return Response.success(stats);
    }
    
    /**
     * 检查订单项是否可以评价
     */
    @GetMapping("/check/{orderItemId}")
    public Response<Boolean> canReviewOrderItem(@PathVariable Integer orderItemId, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        boolean canReview = reviewService.canReviewOrderItem(user.getId(), orderItemId);
        return Response.success(canReview);
    }
} 