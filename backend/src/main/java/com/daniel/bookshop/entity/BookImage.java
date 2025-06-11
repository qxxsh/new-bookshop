package com.daniel.bookshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bookimage")
public class BookImage {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer bid;
    private Integer type;
} 