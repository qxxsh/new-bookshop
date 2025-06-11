package com.daniel.bookshop.shiro;

import com.daniel.bookshop.entity.User;
import com.daniel.bookshop.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
        // 添加角色和权限
        authorizationInfo.addRole("user");
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String studentId = (String) token.getPrincipal();
        User user = userService.getByStudentId(studentId);
        
        if (user == null) {
            throw new UnknownAccountException("账号不存在");
        }
        
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
} 