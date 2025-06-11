package com.daniel.bookshop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daniel.bookshop.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
    
    @Select("SELECT f.*, b.name as book_name, b.author, b.price, b.originalPrice, b.description, " +
            "u.name as user_name FROM favorite f " +
            "LEFT JOIN book b ON f.bid = b.id " +
            "LEFT JOIN user u ON f.uid = u.id " +
            "WHERE f.uid = #{uid} ORDER BY f.createTime DESC")
    List<Favorite> getFavoritesByUserId(@Param("uid") Integer uid);
    
    @Select("SELECT COUNT(*) FROM favorite WHERE uid = #{uid} AND bid = #{bid}")
    int isFavorite(@Param("uid") Integer uid, @Param("bid") Integer bid);
} 