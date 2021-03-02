package com.example.interceptor;

import com.example.models.Users;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInfoInterceptor implements HandlerInterceptor {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("pre Handle");
        try {
            String userToken = request.getHeader("user_token");
            if(userToken.equals(null)){
                System.out.println("Access Token is empty");
                response.encodeURL("Access Token is empty");
                return false;
            }
            Users users = usersRepository.getUserInfo(userToken);
            if(users==null){
                System.out.println("Access Token is not correct");
                response.encodeURL("Access Token is not correct");
                return false;
            }
            System.out.println("Access Token is correct");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("post Handle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("after Handle");
    }
}
