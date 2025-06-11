package com.daniel.bookshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daniel.bookshop.entity.User;

public interface UserService extends IService<User> {
    User getByStudentId(String studentId);
} 