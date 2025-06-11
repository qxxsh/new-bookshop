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
@TableName("`order`")  // order是MySQL关键字，需要加反引号
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("order_no")
    private String orderNo;
    
    @TableField("user_id")
    private Integer userId;
    
    @TableField("total_amount")
    private BigDecimal totalAmount;
    
    private Integer status;
    
    @TableField("payment_method")
    private String paymentMethod;
    
    @TableField("payment_time")
    private LocalDateTime paymentTime;
    
    @TableField("delivery_address")
    private String deliveryAddress;
    
    @TableField("delivery_phone")
    private String deliveryPhone;
    
    @TableField("delivery_name")
    private String deliveryName;
    
    private String remark;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    // 关联信息（不存储在数据库中）
    @TableField(exist = false)
    private User user;
    
    @TableField(exist = false)
    private List<OrderItem> orderItems;
    
    @TableField(exist = false)
    private String statusText;
    
    // 设置状态描述
    public String getStatusText() {
        if (status == null) return "";
        switch (status) {
            case 1: return "待支付";
            case 2: return "已支付";
            case 3: return "已发货";
            case 4: return "已完成";
            case 5: return "已取消";
            default: return "未知状态";
        }
    }
} 