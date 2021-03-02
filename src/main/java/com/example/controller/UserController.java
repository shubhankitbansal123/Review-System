package com.example.controller;


import com.example.models.UserInfo;
import com.example.models.Users;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signUp")// Everybody access and create user with unique emailId
    public String createUser(@RequestBody Users users){

        if(StringUtils.isEmpty(users.getEmail()) || StringUtils.isEmpty(users.getType()) || StringUtils.isEmpty(users.is_admin()) || StringUtils.isEmpty(users.getUsername())) {
            return "Some fields are missing";
        }
        if(!users.getType().equalsIgnoreCase("Hotel") && StringUtils.isEmpty(users.getPassword())){
            return "For " +users.getType()+" password is necessary";
        }
        boolean isUserExist = userService.isUserAlreadyExist(users.getEmail());
        if(isUserExist){
            return "User Already Exist";
        }
        else {
            String userToken = UUID.randomUUID().toString();
            users.setUsertoken(userToken);
            userService.save(users,userToken);
            return "Hello " + users.getUsername() + "\nUser Token : " + users.getUsertoken();
        }
    }

    @PostMapping("/signIn")  // Everybody signIn with correct emailId and password
    public String signIn(@RequestBody Users users){
        Users users1 = userService.getUserByEmail(users.getEmail());
        if(users1==null){
            return "User does not exist";
        }
        else if(!StringUtils.isEmpty(users1.getUsertoken())){
            return "User already signed in";
        }
        else {
            String userToken = UUID.randomUUID().toString();
            users1.setUsertoken(userToken);
            System.out.println(users1.toString());
            try {
                userService.save(users1,userToken);
            }catch (Exception e){
                e.printStackTrace();
                return "Update Unsuccessful";
            }
            return userToken;
        }
    }
    
    @GetMapping("/userInfo")
    public UserInfo userInfo(@RequestHeader("user_token") String userToken){
        System.out.println("in user info");
        Users users =userService.getUserInfo(userToken);
        UserInfo userInfo = new UserInfo(users.getEmail(),users.getUsername(),users.is_admin());
        return userInfo;
    }

    @DeleteMapping("/deleteUser")     // it gives an error
    public String deleteUser(@RequestHeader("user_token") String userToken){
        try{
            if(userService.deleteUser(userToken)!=null)
                return "Delete Successfully";
            else
                return "Deleted Unsuccessful";
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @DeleteMapping("/deleteUserByAdmin")  // delete user by admin
    public String deleteUserByAdmin(@RequestHeader("user_token") String userToken,@RequestParam("email") String email){
        String  userToken1 = userService.getUserTokenByEmail(email);
        if(userToken1==null){
            return "user does not exist";
        }
        userService.deleteUser(userToken1); // work fine but shown give error for no results were returned by the query
        return "Deleted Successfully";
    }

    @DeleteMapping("/logout")
    public String logout(@RequestHeader("user_token") String userToken){
        userService.logout(userToken);
        return "Log out Successful";
    }
}
