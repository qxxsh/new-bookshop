package com.daniel.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daniel.bookshop.dao.FavoriteMapper;
import com.daniel.bookshop.entity.Favorite;
import com.daniel.bookshop.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
    
    @Autowired
    private FavoriteMapper favoriteMapper;
    
    @Override
    public boolean addFavorite(Integer uid, Integer bid) {
        // 检查是否已收藏
        if (isFavorite(uid, bid)) {
            return false;
        }
        
        Favorite favorite = new Favorite();
        favorite.setUid(uid);
        favorite.setBid(bid);
        favorite.setCreateTime(LocalDateTime.now());
        
        return save(favorite);
    }
    
    @Override
    public boolean removeFavorite(Integer uid, Integer bid) {
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).eq("bid", bid);
        return remove(wrapper);
    }
    
    @Override
    public List<Favorite> getFavoritesByUserId(Integer uid) {
        return favoriteMapper.getFavoritesByUserId(uid);
    }
    
    @Override
    public boolean isFavorite(Integer uid, Integer bid) {
        return favoriteMapper.isFavorite(uid, bid) > 0;
    }
    
    @Override
    public boolean toggleFavorite(Integer uid, Integer bid) {
        if (isFavorite(uid, bid)) {
            removeFavorite(uid, bid);
            return false;
        } else {
            addFavorite(uid, bid);
            return true;
        }
    }
} 