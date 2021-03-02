package com.example.service;

import com.example.controller.UserController;
import com.example.models.RatingAverage;
import com.example.models.Users;
import com.example.repository.UsersRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@Service
@EnableCaching
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserController userController;

    public boolean isUserAlreadyExist(String email){
        return usersRepository.userAlreadyExist(email);
    }

    @CachePut(key = "#userToken",value = "users",unless = "#result==null")
    public Users save(Users users,String userToken) {
        System.out.println("user save service");
        usersRepository.save(users);
        return users;
    }

    public boolean isLegitUser(String email) {
        return usersRepository.legitUser(email);
    }

    @Cacheable(key = "#userToken",value = "users",unless = "#result==null")
    public Users getUserInfo(String userToken) {
        System.out.println("userinfo service");
        Users users = usersRepository.getUserInfo(userToken);
        return users;
    }

    @CacheEvict(key = "#userToken",value = "users")
    public Users deleteUser(String userToken) {
        System.out.println("in delete user");
        return usersRepository.deleteUser(userToken);
    }

    public boolean legitUser(String email) {
        return usersRepository.legitUser(email);
    }

    public boolean deleteUserByEmail(String email) {
        return usersRepository.deleteUserByEmail(email);
    }

    @CacheEvict(key = "#userToken",value = "users")
    public Users logout(String userToken) {
        Users users = usersRepository.getUserInfo(userToken);
        usersRepository.logout(userToken);
        return users;
    }

    public boolean checkUser(String userToken, Integer user_id, String type) {
        return usersRepository.checkUser(userToken,user_id,type);
    }

    public Integer getUserIdFromUserToken(String userToken) {
        return usersRepository.getUserIdFromUserToken(userToken);
    }

    public boolean checkUserToken(String userToken) {
        System.out.println("user access token");
        return usersRepository.checkUserToken(userToken);
    }

    @Cacheable(key = "#userToken" ,value = "users",unless = "#result==null")
    public Users checkAdmin(String userToken) {
        System.out.println("check admin");
        return usersRepository.checkAdmin(userToken);
    }

    public Users getUserByEmail(String email) {
        return usersRepository.getUser(email);
    }

    public String getUserTokenByEmail(String email) {
        return usersRepository.getUserTokenByEmail(email);
    }
}
