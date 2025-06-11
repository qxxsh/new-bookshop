package com.daniel.bookshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_item")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("order_id")
    private Integer orderId;
    
    @TableField("book_id")
    private Integer bookId;
    
    @TableField("book_name")
    private String bookName;
    
    @TableField("book_author")
    private String bookAuthor;
    
    @TableField("book_price")
    private BigDecimal bookPrice;
    
    private Integer quantity;
    
    private BigDecimal subtotal;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    // 关联的书籍信息（不存储在数据库中）
    @TableField(exist = false)
    private Book book;
} 