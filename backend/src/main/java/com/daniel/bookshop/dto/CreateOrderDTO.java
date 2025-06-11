package com.daniel.bookshop.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {
    /**
     * 购物车项ID列表（从购物车创建订单时使用）
     */
    private List<Integer> cartIds;
    
    /**
     * 收货地址
     */
    private String deliveryAddress;
    
    /**
     * 收货电话
     */
    private String deliveryPhone;
    
    /**
     * 收货人姓名
     */
    private String deliveryName;
    
    /**
     * 订单备注
     */
    private String remark;
} 