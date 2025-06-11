package com.daniel.bookshop.controller;

import com.daniel.bookshop.common.Constants;
import com.daniel.bookshop.common.Response;
import com.daniel.bookshop.entity.User;
import com.daniel.bookshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Response<User> register(@RequestBody User user) {
        // 检查用户名是否已存在
        if (userService.getByStudentId(user.getStudentid()) != null) {
            return Response.error("学号已存在");
        }
        boolean success = userService.save(user);
        return success ? Response.success(user) : Response.error("注册失败");
    }

    @PostMapping("/login")
    public Response<User> login(@RequestBody User user, HttpSession session) {
        User dbUser = userService.getByStudentId(user.getStudentid());
        if (dbUser == null) {
            return Response.error("用户不存在");
        }
        if (!dbUser.getPassword().equals(user.getPassword())) {
            return Response.error("密码错误");
        }
        session.setAttribute(Constants.SESSION_USER, dbUser);
        return Response.success(dbUser);
    }

    @GetMapping("/info")
    public Response<User> getInfo(HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        return user != null ? Response.success(user) : Response.error("未登录");
    }

    @PostMapping("/logout")
    public Response<Void> logout(HttpSession session) {
        session.removeAttribute(Constants.SESSION_USER);
        return Response.success();
    }

    @PutMapping
    public Response<User> update(@RequestBody User user) {
        boolean success = userService.updateById(user);
        return success ? Response.success(user) : Response.error("更新失败");
    }

    @PutMapping("/update")
    public Response<User> updateUserInfo(@RequestBody User user, HttpSession session) {
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);
        if (currentUser == null) {
            return Response.error("未登录");
        }
        
        // 更新用户信息
        user.setId(currentUser.getId());
        boolean success = userService.updateById(user);
        if (success) {
            // 更新session中的用户信息
            User updatedUser = userService.getById(currentUser.getId());
            session.setAttribute(Constants.SESSION_USER, updatedUser);
            return Response.success(updatedUser);
        }
        return Response.error("更新失败");
    }
} 