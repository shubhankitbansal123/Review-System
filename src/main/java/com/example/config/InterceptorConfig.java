package com.example.config;

import com.example.interceptor.AdminInterceptor;
import com.example.interceptor.UserInfoInterceptor;
import com.example.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Component
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private UserInfoInterceptor userInfoInterceptor;

    @Autowired
    private UserInterceptor userInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor).addPathPatterns(Arrays.asList("/deleteUserByAdmin","/deleteHotel"));
        registry.addInterceptor(userInterceptor).addPathPatterns(Arrays.asList("/userInfo","/deleteUser","/createComment","/editComment","/logout","/editComment","/deleteComment","/getComment","/rateHotel"));
        System.out.println("add interceptor Handle");
    }
}
