package com.daniel.bookshop.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 书籍推荐DTO
 */
@Data
public class BookRecommendationDto {
    
    /**
     * 购物车中的书籍标题列表
     */
    private List<String> cartBookTitles;
    
    /**
     * AI推荐理由
     */
    private String recommendationReason;
    
    /**
     * 推荐的书籍列表
     */
    private List<RecommendedBook> recommendedBooks;
    
    @Data
    public static class RecommendedBook {
        /**
         * 书籍标题
         */
        private String title;
        
        /**
         * 作者
         */
        private String author;
        
        /**
         * 推荐理由
         */
        private String reason;
        
        /**
         * 是否在数据库中存在
         */
        private boolean existsInDatabase;
        
        /**
         * 数据库中的书籍ID（如果存在）
         */
        @JsonProperty("bookId")
        private Integer bookId;
        
        /**
         * 价格（如果在数据库中存在）
         */
        private BigDecimal price;
        
        /**
         * 品相（如果在数据库中存在）
         */
        private BigDecimal degree;
        
        /**
         * 书籍描述（如果在数据库中存在）
         */
        private String description;
    }
} 