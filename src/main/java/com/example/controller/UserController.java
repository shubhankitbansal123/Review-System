package com.example.controller;


import com.example.models.UserInfo;
import com.example.models.Users;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/signUp")// Everybody access and create user with unique emailId //Done
    public String createUser(@RequestBody Users users) {
        if(StringUtils.isEmpty(users.getType())){
            return "Some fields are missing";
        }
        if(users.getType().equalsIgnoreCase("Hotel")){
            if(StringUtils.isEmpty(users.getEmail())  || StringUtils.isEmpty(users.isIsadmin()) || StringUtils.isEmpty(users.getUsername())) {
                return "Some fields are missing";
            }
            boolean isUserExist = userService.isUserAlreadyExist(users.getEmail());
            if(isUserExist){
                return "User Already Exist";
            }
            else {
                String userToken = UUID.randomUUID().toString();
                users.setUsertoken(userToken);
                userService.save(users, userToken);
                return "Hello " + users.getUsername() + "\nUser Token : " + users.getUsertoken();
            }
        }
        else{
            return "No need to sign up";
        }
    }

    @PostMapping("/signIn")  // Everybody signIn with correct emailId and password  //Done
    public String signIn(@RequestBody Users users){

        if(StringUtils.isEmpty(users.getEmail()) && StringUtils.isEmpty(users.getUsername())){
            return "Some fields are missing";
        }
        if(StringUtils.isEmpty(users.getEmail())){
            if(StringUtils.isEmpty(users.getPassword())){
                return "Password is empty";
            }
            else {
                users=userService.getByUsernameAndPassword(users.getUsername(),users.getPassword());
                if(users==null){
                    return "User does not exist";
                }
                if(users.getType().equalsIgnoreCase("Hotel")){
                    return "User type hotel need to give email for signin";
                }
                if(!StringUtils.isEmpty(users.getUsertoken())){
                    return users.getUsertoken();
                }
                else {
                    String userToken = UUID.randomUUID().toString();
                    users.setUsertoken(userToken);
                    userService.save(users,userToken);
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
                return "User type " + users.getType() +"need to give username and password for sign in";
            }
            if(!StringUtils.isEmpty(users.getUsertoken())){
                return users.getUsertoken();
            }
            else {
                String userToken = UUID.randomUUID().toString();
                users.setUsertoken(userToken);
                System.out.println(users.toString());
                userService.save(users, userToken);
                return userToken;
            }
        }
        else {
            return "Something is wrong";
        }
    }

    
    @GetMapping("/userInfo")  // Done
    public ResponseEntity<Object> userInfo(@RequestHeader("user_token") String userToken){
        System.out.println("in user info");
        Users users =userService.getUserInfo(userToken);
        UserInfo userInfo = new UserInfo(users.getEmail(),users.getUsername(),users.isIsadmin());
        return new ResponseEntity<>(userInfo, HttpStatus.ACCEPTED);
//        return userInfo;
    }

    @DeleteMapping("/deleteUser")     // it gives an error  //Done
    public String deleteUser(@RequestHeader("user_token") String userToken){
        userService.deleteUser(userToken);
        return "Delete Successfully";
    }

    @DeleteMapping("/deleteUserByAdmin")  // delete user by admin //Done
    public String deleteUserByAdmin(@RequestHeader("user_token") String userToken,@RequestParam("email") String email){
        String  userToken1 = userService.getUserTokenByEmail(email);
        if(userToken1==null){
            return "user does not exist";
        }
        userService.deleteUser(userToken1); // work fine but shown give error for no results were returned by the query
        return "Deleted Successfully";

    }

    @DeleteMapping("/logout")  //Done
    public String logout(@RequestHeader("user_token") String userToken){
        userService.logout(userToken);
        return "Log out Successful";

    }
}
