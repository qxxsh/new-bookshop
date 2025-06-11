package com.daniel.bookshop.controller;

import com.daniel.bookshop.common.Constants;
import com.daniel.bookshop.common.Response;
import com.daniel.bookshop.entity.Cart;
import com.daniel.bookshop.entity.User;
import com.daniel.bookshop.service.CartService;
import com.daniel.bookshop.service.BookRecommendationService;
import com.daniel.bookshop.config.QianfanConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private BookRecommendationService bookRecommendationService;
    
    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    public Response<Void> addToCart(@RequestParam Integer bookId, 
                                   @RequestParam(defaultValue = "1") Integer quantity,
                                   HttpSession session) {
        System.out.println("=== 购物车调试信息 ===");
        System.out.println("接收到的bookId: " + bookId);
        System.out.println("接收到的quantity: " + quantity);
        
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            System.out.println("用户未登录");
            return Response.error("请先登录");
        }
        
        System.out.println("当前用户ID: " + user.getId());
        System.out.println("当前用户姓名: " + user.getName());
        
        boolean success = cartService.addToCart(user.getId(), bookId, quantity);
        System.out.println("添加购物车结果: " + success);
        
        if (success) {
            return Response.success();
        } else {
            return Response.error("添加失败");
        }
    }
    
    /**
     * 获取购物车列表
     */
    @GetMapping("/list")
    public Response<List<Cart>> getCartList(HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        List<Cart> cartList = cartService.getCartList(user.getId());
        return Response.success(cartList);
    }
    
    /**
     * 更新购物车商品数量
     */
    @PutMapping("/update")
    public Response<Void> updateQuantity(@RequestParam Integer cartId,
                                        @RequestParam Integer quantity,
                                        HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        if (quantity <= 0) {
            return Response.error("数量必须大于0");
        }
        
        boolean success = cartService.updateQuantity(cartId, quantity, user.getId());
        if (success) {
            return Response.success();
        } else {
            return Response.error("更新失败");
        }
    }
    
    /**
     * 从购物车删除商品
     */
    @DeleteMapping("/remove/{cartId}")
    public Response<Void> removeFromCart(@PathVariable Integer cartId, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        boolean success = cartService.removeFromCart(cartId, user.getId());
        if (success) {
            return Response.success();
        } else {
            return Response.error("删除失败");
        }
    }
    
    /**
     * 清空购物车
     */
    @DeleteMapping("/clear")
    public Response<Void> clearCart(HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        boolean success = cartService.clearCart(user.getId());
        if (success) {
            return Response.success();
        } else {
            return Response.error("清空失败");
        }
    }
    
    /**
     * 获取购物车商品数量
     */
    @GetMapping("/count")
    public Response<Integer> getCartCount(HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.success(0);
        }
        
        Integer count = cartService.getCartCount(user.getId());
        return Response.success(count);
    }
    
    /**
     * 调试接口：查看购物车详细信息
     */
    @GetMapping("/debug")
    public Response<Object> debugCart(HttpSession session) {
        try {
            User user = (User) session.getAttribute(Constants.SESSION_USER);
            if (user == null) {
                return Response.error("请先登录");
            }
            
            System.out.println("=== 购物车调试接口 ===");
            System.out.println("用户ID: " + user.getId());
            System.out.println("用户名: " + user.getName());
            
            List<Cart> cartList = cartService.getCartList(user.getId());
            System.out.println("购物车记录数: " + cartList.size());
            
            java.util.Map<String, Object> debugInfo = new java.util.HashMap<>();
            debugInfo.put("userId", user.getId());
            debugInfo.put("userName", user.getName());
            debugInfo.put("cartCount", cartList.size());
            debugInfo.put("cartList", cartList);
            
            return Response.success(debugInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("调试失败: " + e.getMessage());
        }
    }
} 