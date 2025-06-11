package com.daniel.bookshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cart")
public class Cart {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("user_id")
    private Integer userId;
    
    @TableField("book_id")
    private Integer bookId;
    
    private Integer quantity;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    // 关联的书籍信息（不存储在数据库中）
    @TableField(exist = false)
    private Book book;
    
    // 关联的用户信息（不存储在数据库中）
    @TableField(exist = false)
    private User user;
} 