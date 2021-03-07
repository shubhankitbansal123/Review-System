package com.example.service;

import com.example.models.Users;
import com.example.repository.UsersRepository;
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
    private final UsersRepository usersRepository;


    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean isUserAlreadyExist(String email){
        return usersRepository.userAlreadyExist(email);
    }

    @CachePut(key = "#userToken",value = "users",unless = "#result==null")
    public Users save(Users users,String userToken) {
        System.out.println("user save service");
        usersRepository.save(users);
        return users;
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

    @CacheEvict(key = "#userToken",value = "users")
    public Users logout(String userToken) {
        Users users = usersRepository.getUserInfo(userToken);
        usersRepository.logout(userToken);
        return users;
    }

    public Users getUserByEmail(String email) {
        return usersRepository.getUser(email);
    }

    public String getUserTokenByEmail(String email) {
        return usersRepository.getUserTokenByEmail(email);
    }

    public Users getByUsernameAndPassword(String username, String password) {
        return usersRepository.getByUsernameAndPassword(username,password);
    }
}
