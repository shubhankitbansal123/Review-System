package com.example.interceptor;


import com.example.models.Users;
import com.example.repository.UsersRepository;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(AdminInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (StringUtils.isEmpty(request.getHeader("user_token"))) {
                logger.error("Header is empty");
                response.getWriter().write("Header is empty");
                return false;
            }
            String userToken = request.getHeader("user_token");
            Users users = userService.getUserInfo(userToken);
            if (users == null || !users.isIsadmin()) {
                logger.error("User does not exist or not Admin");
                response.getWriter().write("User does not exist or not Admin");
                return false;
            }
            return true;
        }catch (Exception e){
            logger.error("Something is wrong");
            response.getWriter().write("Something is wrong");
            return true;
        }
    }
}
