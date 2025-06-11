package com.daniel.bookshop.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daniel.bookshop.common.Constants;
import com.daniel.bookshop.common.Response;
import com.daniel.bookshop.dto.CreateOrderDTO;
import com.daniel.bookshop.entity.Order;
import com.daniel.bookshop.entity.User;
import com.daniel.bookshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 从购物车创建订单
     */
    @PostMapping("/create")
    public Response<Order> createOrder(@RequestBody CreateOrderDTO createOrderDTO, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        try {
            Order order = orderService.createOrderFromCart(user.getId(), createOrderDTO);
            return Response.success(order);
        } catch (Exception e) {
            return Response.error("创建订单失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取用户订单列表
     */
    @GetMapping("/list")
    public Response<Page<Order>> getOrderList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status,
            HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        Page<Order> orderPage = orderService.getUserOrders(user.getId(), pageNum, pageSize, status);
        return Response.success(orderPage);
    }
    
    /**
     * 获取订单详情
     */
    @GetMapping("/{orderId}")
    public Response<Order> getOrderDetail(@PathVariable Integer orderId, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        Order order = orderService.getOrderDetail(orderId, user.getId());
        if (order == null) {
            return Response.error("订单不存在");
        }
        
        return Response.success(order);
    }
    
    /**
     * 取消订单
     */
    @PutMapping("/{orderId}/cancel")
    public Response<Void> cancelOrder(@PathVariable Integer orderId, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        boolean success = orderService.cancelOrder(orderId, user.getId());
        if (success) {
            return Response.success();
        } else {
            return Response.error("取消订单失败");
        }
    }
    
    /**
     * 确认收货
     */
    @PutMapping("/{orderId}/confirm")
    public Response<Void> confirmOrder(@PathVariable Integer orderId, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        boolean success = orderService.confirmOrder(orderId, user.getId());
        if (success) {
            return Response.success();
        } else {
            return Response.error("确认收货失败");
        }
    }
    
    /**
     * 支付订单
     */
    @PutMapping("/{orderId}/pay")
    public Response<Void> payOrder(@PathVariable Integer orderId,
                                  @RequestParam String paymentMethod,
                                  HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return Response.error("请先登录");
        }
        
        boolean success = orderService.payOrder(orderId, user.getId(), paymentMethod);
        if (success) {
            return Response.success();
        } else {
            return Response.error("支付失败");
        }
    }
} 