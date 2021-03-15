package com.example.interceptor;


import com.example.models.Users;
import com.example.repository.UsersRepository;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(StringUtils.isEmpty(request.getHeader("user_token"))){
            return false;
        }
        String userToken = request.getHeader("user_token");
        Users users = userService.getUserInfo(userToken);
        if(users==null || !users.isIsadmin()){
            System.out.println("User does not exist or not admin");
            return false;
        }
        System.out.println("in admin interceptor");
        return true;
    }
}
