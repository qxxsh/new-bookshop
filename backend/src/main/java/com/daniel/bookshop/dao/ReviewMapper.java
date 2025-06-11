package com.daniel.bookshop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daniel.bookshop.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
    
    /**
     * 获取书籍的评价列表（包含用户信息）
     */
    @Select("SELECT r.*, u.name as user_name " +
            "FROM review r LEFT JOIN user u ON r.user_id = u.id " +
            "WHERE r.book_id = #{bookId} " +
            "ORDER BY r.create_time DESC")
    List<Review> getBookReviews(Integer bookId);
    
    /**
     * 获取用户的评价列表（包含书籍信息）
     */
    @Select("SELECT r.*, b.name as book_name " +
            "FROM review r LEFT JOIN book b ON r.book_id = b.id " +
            "WHERE r.user_id = #{userId} " +
            "ORDER BY r.create_time DESC")
    List<Review> getUserReviews(Integer userId);
    
    /**
     * 获取书籍的平均评分
     */
    @Select("SELECT AVG(rating) FROM review WHERE book_id = #{bookId}")
    Double getBookAverageRating(Integer bookId);
    
    /**
     * 获取书籍的评价统计
     */
    @Select("SELECT rating, COUNT(*) as count " +
            "FROM review WHERE book_id = #{bookId} " +
            "GROUP BY rating ORDER BY rating DESC")
    List<Object> getBookRatingStats(Integer bookId);
} 