package com.daniel.bookshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("book")
public class Book {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer cid;
    @TableField("bookType")
    private Integer bookType;
    private BigDecimal price;
    @TableField("originalPrice")
    private BigDecimal originalPrice;
    private Integer uid;
    private String author;
    private String press;
    private String version;
    private BigDecimal degree;
    @TableField("publishDate")
    private String publishDate;
    private String description;
    private LocalDateTime date;

    @TableField(exist = false)
    private Category category;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private BookImage bookImage;
    @TableField(exist = false)
    private List<BookImage> bookImages;
} 