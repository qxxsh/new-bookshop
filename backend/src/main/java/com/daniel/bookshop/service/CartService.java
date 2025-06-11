package com.daniel.bookshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daniel.bookshop.entity.Cart;

import java.util.List;

public interface CartService extends IService<Cart> {
    
    /**
     * 添加商品到购物车
     */
    boolean addToCart(Integer userId, Integer bookId, Integer quantity);
    
    /**
     * 获取用户购物车列表
     */
    List<Cart> getCartList(Integer userId);
    
    /**
     * 更新购物车商品数量
     */
    boolean updateQuantity(Integer cartId, Integer quantity, Integer userId);
    
    /**
     * 从购物车删除商品
     */
    boolean removeFromCart(Integer cartId, Integer userId);
    
    /**
     * 清空购物车
     */
    boolean clearCart(Integer userId);
    
    /**
     * 获取购物车商品数量
     */
    Integer getCartCount(Integer userId);
} 