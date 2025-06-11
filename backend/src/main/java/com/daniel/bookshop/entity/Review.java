package com.daniel.bookshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("review")
public class Review {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private Integer orderId;
    
    private Integer orderItemId;
    
    private Integer bookId;
    
    private Integer userId;
    
    private Integer rating;
    
    private String content;
    
    private String images;
    
    private Boolean isAnonymous;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    // 关联信息（不存储在数据库中）
    @TableField(exist = false)
    private User user;
    
    @TableField(exist = false)
    private Book book;
    
    @TableField(exist = false)
    private Order order;
    
    @TableField(exist = false)
    private OrderItem orderItem;
} 