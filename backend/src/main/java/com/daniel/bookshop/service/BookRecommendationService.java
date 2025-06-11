package com.daniel.bookshop.service;

import com.daniel.bookshop.dto.BookRecommendationDto;

/**
 * 书籍推荐服务
 */
public interface BookRecommendationService {
    
    /**
     * 基于用户购物车推荐书籍
     * @param userId 用户ID
     * @return 推荐结果
     */
    BookRecommendationDto recommendBooksBasedOnCart(Integer userId);
} 