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

    @PostMapping("/signUp")
    public ResponseEntity<Object> createUser(@RequestBody Users users) {
        try {
            if (ObjectUtils.isEmpty(users.getType())) {
                return new ResponseEntity<>("User type are missing",HttpStatus.NOT_FOUND);
            }
            if (users.getType().equalsIgnoreCase("Hotel")) {
                if (ObjectUtils.isEmpty(users.getEmail()) || ObjectUtils.isEmpty(users.isIsadmin()) || ObjectUtils.isEmpty(users.getUsername())) {
                    return new ResponseEntity<>("Some fields are missing",HttpStatus.NOT_FOUND);
                }
                boolean isUserExist = userService.isUserAlreadyExist(users.getEmail());
                if (isUserExist) {
                    return new ResponseEntity<>("User Already Exist",HttpStatus.CONFLICT);
                } else {
                    String userToken = UUID.randomUUID().toString();
                    users.setUsertoken(userToken);
                    userService.save(users, userToken);
                    return new ResponseEntity<>("Hello " + users.getUsername() + "\nUser Token : " + users.getUsertoken(),HttpStatus.ACCEPTED);
                }
            } else {
                return new ResponseEntity<>("No need to sign up",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Something is wrong",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<Object> signIn(@RequestBody Users users){

        try {
            if (ObjectUtils.isEmpty(users.getEmail()) && ObjectUtils.isEmpty(users.getUsername())) {
                return new ResponseEntity<>("Some fields are missing",HttpStatus.NOT_FOUND);
            }
            if(ObjectUtils.isEmpty(users.getPassword())){
                return new ResponseEntity<>("Password is Empty",HttpStatus.NOT_FOUND);
            }
            if (ObjectUtils.isEmpty(users.getEmail())) {
                users = userService.getByUsernameAndPassword(users.getUsername(), users.getPassword());
                if (users == null) {
                    return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);
                }
                if (users.getType().equalsIgnoreCase("Hotel")) {
                    return new ResponseEntity<>("User type hotel need to give email and password for sign in",HttpStatus.BAD_REQUEST);
                }
                if (!ObjectUtils.isEmpty(users.getUsertoken())) {
                    return new ResponseEntity<>(users.getUsertoken(),HttpStatus.ACCEPTED);
                } else {
                    String userToken = UUID.randomUUID().toString();
                    users.setUsertoken(userToken);
                    userService.save(users, userToken);
                    return new ResponseEntity<>(userToken,HttpStatus.ACCEPTED);
                }
            }
            if (ObjectUtils.isEmpty(users.getUsername())) {
                users = userService.getUserByEmailAndPassword(users.getEmail(),users.getPassword());
                if (users == null) {
                    return new ResponseEntity<>("Email or Password is incorrect",HttpStatus.NOT_FOUND);
                }
                if (!users.getType().equalsIgnoreCase("Hotel")) {
                    return new ResponseEntity<>("User type " + users.getType() + "need to give username and password for sign in",HttpStatus.BAD_REQUEST);
                }
                if (!ObjectUtils.isEmpty(users.getUsertoken())) {
                    return new ResponseEntity<>(users.getUsertoken(),HttpStatus.ACCEPTED);
                } else {
                    String userToken = UUID.randomUUID().toString();
                    users.setUsertoken(userToken);
                    System.out.println(users.toString());
                    userService.save(users, userToken);
                    return new ResponseEntity<>(userToken,HttpStatus.ACCEPTED);
                }
            } else {
                return new ResponseEntity<>("Some information is Extra",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Something is wrong",HttpStatus.BAD_REQUEST);
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

    @DeleteMapping("/deleteUser")
    public ResponseEntity<Object> deleteUser(@RequestHeader("user_token") String userToken){
        try {
            userService.deleteUser(userToken);
            return new ResponseEntity<>("Delete Successfully",HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Delete Unsuccessful",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUserByAdmin")
    public ResponseEntity<Object> deleteUserByAdmin(@RequestHeader("user_token") String userToken,@RequestParam("email") String email){
        try {
            String userToken1 = userService.getUserTokenByEmail(email);
            if (userToken1 == null) {
                return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);
            }
            userService.deleteUser(userToken1); // work fine but shown give error for no results were returned by the query
            return new ResponseEntity<>("Delete Successfully",HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Delete Unsuccessful",HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader("user_token") String userToken){
        try {
            userService.logout(userToken);
            return new ResponseEntity<>("Logout Successful",HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Logout Unsuccessful",HttpStatus.BAD_REQUEST);
        }

    }
}
