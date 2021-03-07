package com.example.controller;


import com.example.models.UserInfo;
import com.example.models.Users;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signUp")// Everybody access and create user with unique emailId
    public String createUser(@RequestBody Users users){

        if(StringUtils.isEmpty(users.getType())){
            return "Some fields are missing";
        }
        if(users.getType().equalsIgnoreCase("Hotel")){
            if(StringUtils.isEmpty(users.getEmail())  || StringUtils.isEmpty(users.is_admin()) || StringUtils.isEmpty(users.getUsername())) {
                return "Some fields are missing";
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
        else{
            return "No need to sign up";
        }
    }

    @PostMapping("/signIn")  // Everybody signIn with correct emailId and password
    public String signIn(@RequestBody Users users){
        if(StringUtils.isEmpty(users.getEmail()) && StringUtils.isEmpty(users.getUsername())){
            return "Some fields are missing";
        }
        if(StringUtils.isEmpty(users.getEmail())){
            if(StringUtils.isEmpty(users.getPassword())){
                return "Password is empty";
            }
            else {
                users = userService.getByUsernameAndPassword(users.getUsername(),users.getPassword());
                if(users==null){
                    return "User does not exist";
                }
                if(users.getType().equalsIgnoreCase("Hotel")){
                    return "Something is wrong";
                }
                if(!StringUtils.isEmpty(users.getUsertoken())){
                    return users.getUsertoken();
                }
                else {
                    String userToken = UUID.randomUUID().toString();
                    users.setUsertoken(userToken);
                    try {
                        userService.save(users,userToken);
                    }catch (Exception e){
                        e.printStackTrace();
                        return "Update Unsuccessful";
                    }
                    return userToken;
                }
            }
        }
        if(StringUtils.isEmpty(users.getUsername())){
            users = userService.getUserByEmail(users.getEmail());
            if(users==null){
                return "User does not exist";
            }
            if(!users.getType().equalsIgnoreCase("Hotel")){
                return "Something is wrong";
            }
            if(!StringUtils.isEmpty(users.getUsertoken())){
                return users.getUsertoken();
            }
            else {
                String userToken = UUID.randomUUID().toString();
                users.setUsertoken(userToken);
                System.out.println(users.toString());
                try {
                    userService.save(users,userToken);
                }catch (Exception e){
                    e.printStackTrace();
                    return "Update Unsuccessful";
                }
                return userToken;
            }
        }
        else {
            return "Something is wrong";
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
