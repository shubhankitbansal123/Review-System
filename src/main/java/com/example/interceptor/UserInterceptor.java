package com.example.interceptor;


import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private UsersRepository usersRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getHeader("access_token").isEmpty()){
            return false;
        }
        String accessToken = request.getHeader("access_token");
        if(usersRepository.checkAccessToken(accessToken)==0){
            return false;
        }
        return true;
    }
}
