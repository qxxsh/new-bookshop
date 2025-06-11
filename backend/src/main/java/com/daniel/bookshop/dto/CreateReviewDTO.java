package com.daniel.bookshop.dto;

import lombok.Data;

@Data
public class CreateReviewDTO {
    /**
     * 订单项ID
     */
    private Integer orderItemId;
    
    /**
     * 评分（1-5星）
     */
    private Integer rating;
    
    /**
     * 评价内容
     */
    private String content;
    
    /**
     * 是否匿名评价
     */
    private Boolean isAnonymous;
} 