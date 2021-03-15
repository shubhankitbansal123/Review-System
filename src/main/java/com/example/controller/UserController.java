package com.example.controller;


import com.example.models.UserInfo;
import com.example.models.Users;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")// Everybody access and create user with unique emailId //Done
    public String createUser(@RequestBody Users users) {
        try {
            if (ObjectUtils.isEmpty(users.getType())) {
                return "Some fields are missing";
            }
            if (users.getType().equalsIgnoreCase("Hotel")) {
                if (ObjectUtils.isEmpty(users.getEmail()) || ObjectUtils.isEmpty(users.isIsadmin()) || ObjectUtils.isEmpty(users.getUsername())) {
                    return "Some fields are missing";
                }
                boolean isUserExist = userService.isUserAlreadyExist(users.getEmail());
                if (isUserExist) {
                    return "User Already Exist";
                } else {
                    String userToken = UUID.randomUUID().toString();
                    users.setUsertoken(userToken);
                    userService.save(users, userToken);
                    return "Hello " + users.getUsername() + "\nUser Token : " + users.getUsertoken();
                }
            } else {
                return "No need to sign up";
            }
        }catch (Exception e){
            return "Something is wrong";
        }
    }

    @PostMapping("/signIn")  // Everybody signIn with correct emailId and password  //Done
    public String signIn(@RequestBody Users users){

        try {
            if (ObjectUtils.isEmpty(users.getEmail()) && ObjectUtils.isEmpty(users.getUsername())) {
                return "Some fields are missing";
            }
            if (ObjectUtils.isEmpty(users.getEmail())) {
                if (ObjectUtils.isEmpty(users.getPassword())) {
                    return "Password is empty";
                } else {
                    users = userService.getByUsernameAndPassword(users.getUsername(), users.getPassword());
                    if (users == null) {
                        return "User does not exist";
                    }
                    if (users.getType().equalsIgnoreCase("Hotel")) {
                        return "User type hotel need to give email for Sign In";
                    }
                    if (!ObjectUtils.isEmpty(users.getUsertoken())) {
                        return users.getUsertoken();
                    } else {
                        String userToken = UUID.randomUUID().toString();
                        users.setUsertoken(userToken);
                        userService.save(users, userToken);
                        return userToken;
                    }
                }
            }
            if (ObjectUtils.isEmpty(users.getUsername())) {
                users = userService.getUserByEmail(users.getEmail());
                if (users == null) {
                    return "User does not exist";
                }
                if (!users.getType().equalsIgnoreCase("Hotel")) {
                    return "User type " + users.getType() + "need to give username and password for sign in";
                }
                if (!ObjectUtils.isEmpty(users.getUsertoken())) {
                    return users.getUsertoken();
                } else {
                    String userToken = UUID.randomUUID().toString();
                    users.setUsertoken(userToken);
                    System.out.println(users.toString());
                    userService.save(users, userToken);
                    return userToken;
                }
            } else {
                return "Something is wrong";
            }
        }catch (Exception e){
            return "Something is wrong";
        }
    }

    
    @GetMapping("/userInfo")  // Done
    public ResponseEntity<Object> userInfo(@RequestHeader("user_token") String userToken){
        try {

            System.out.println("in user info");
            Users users = userService.getUserInfo(userToken);
            UserInfo userInfo = new UserInfo(users.getEmail(), users.getUsername(), users.isIsadmin());
            return new ResponseEntity<>(userInfo,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Something is wrong",HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteUser")     // it gives an error  //Done
    public String deleteUser(@RequestHeader("user_token") String userToken){
        try {
            userService.deleteUser(userToken);
            return "Delete Successfully";
        }catch (Exception e){
            return "Delete Unsuccessful";
        }
    }

    @DeleteMapping("/deleteUserByAdmin")  // delete user by admin //Done
    public String deleteUserByAdmin(@RequestHeader("user_token") String userToken,@RequestParam("email") String email){
        try {
            String userToken1 = userService.getUserTokenByEmail(email);
            if (userToken1 == null) {
                return "user does not exist";
            }
            userService.deleteUser(userToken1); // work fine but shown give error for no results were returned by the query
            return "Deleted Successfully";
        }catch (Exception e){
            return "Delete Unsuccessful";
        }

    }

    @DeleteMapping("/logout")  //Done
    public String logout(@RequestHeader("user_token") String userToken){
        try {
            userService.logout(userToken);
            return "Log out Successful";
        }catch (Exception e){
            return "Logout not done";
        }

    }
}
