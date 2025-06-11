package com.daniel.bookshop.controller;

import com.daniel.bookshop.common.Response;
import com.daniel.bookshop.entity.Favorite;
import com.daniel.bookshop.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    /**
     * 添加收藏
     */
    @PostMapping("/add")
    public Response<String> addFavorite(@RequestParam Integer uid, @RequestParam Integer bid) {
        boolean success = favoriteService.addFavorite(uid, bid);
        return success ? Response.success("收藏成功") : Response.error("收藏失败，可能已经收藏过了");
    }
    
    /**
     * 取消收藏
     */
    @DeleteMapping("/remove")
    public Response<String> removeFavorite(@RequestParam Integer uid, @RequestParam Integer bid) {
        boolean success = favoriteService.removeFavorite(uid, bid);
        return success ? Response.success("取消收藏成功") : Response.error("取消收藏失败");
    }
    
    /**
     * 切换收藏状态
     */
    @PostMapping("/toggle")
    public Response<Boolean> toggleFavorite(@RequestParam Integer uid, @RequestParam Integer bid) {
        boolean isFavorite = favoriteService.toggleFavorite(uid, bid);
        String message = isFavorite ? "收藏成功" : "取消收藏成功";
        return Response.success(isFavorite);
    }
    
    /**
     * 获取用户收藏列表
     */
    @GetMapping("/list/{uid}")
    public Response<List<Favorite>> getFavoritesByUserId(@PathVariable Integer uid) {
        List<Favorite> favorites = favoriteService.getFavoritesByUserId(uid);
        return Response.success(favorites);
    }
    
    /**
     * 检查是否已收藏
     */
    @GetMapping("/check")
    public Response<Boolean> isFavorite(@RequestParam Integer uid, @RequestParam Integer bid) {
        boolean isFavorite = favoriteService.isFavorite(uid, bid);
        return Response.success(isFavorite);
    }
} 