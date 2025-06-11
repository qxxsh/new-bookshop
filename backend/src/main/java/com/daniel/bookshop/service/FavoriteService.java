package com.daniel.bookshop.service;

import com.daniel.bookshop.entity.Favorite;

import java.util.List;

public interface FavoriteService {
    
    /**
     * 添加收藏
     * @param uid 用户ID
     * @param bid 书籍ID
     * @return 是否成功
     */
    boolean addFavorite(Integer uid, Integer bid);
    
    /**
     * 取消收藏
     * @param uid 用户ID
     * @param bid 书籍ID
     * @return 是否成功
     */
    boolean removeFavorite(Integer uid, Integer bid);
    
    /**
     * 获取用户收藏列表
     * @param uid 用户ID
     * @return 收藏列表
     */
    List<Favorite> getFavoritesByUserId(Integer uid);
    
    /**
     * 检查是否已收藏
     * @param uid 用户ID
     * @param bid 书籍ID
     * @return 是否已收藏
     */
    boolean isFavorite(Integer uid, Integer bid);
    
    /**
     * 切换收藏状态
     * @param uid 用户ID
     * @param bid 书籍ID
     * @return 操作后的收藏状态（true=已收藏，false=未收藏）
     */
    boolean toggleFavorite(Integer uid, Integer bid);
} 