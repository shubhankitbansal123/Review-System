package com.example.interceptor;


import com.example.models.Users;
import com.example.repository.UsersRepository;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.yaml.snakeyaml.tokens.ScalarToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (StringUtils.isEmpty(request.getHeader("user_token"))) {
                response.getWriter().write("Header is empty");
                return false;
            }
            String userToken = request.getHeader("user_token");
            Users users = userService.getUserInfo(userToken);
            if (users == null) {
                response.getWriter().write("User does not exist");
                System.out.println("User does not exist");
                return false;
            }
            return true;
        }catch (Exception e){
            response.getWriter().write("Something is wrong");
            return false;
        }
    }
}
