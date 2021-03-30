package com.example.controller;


import com.example.Exception.*;
import com.example.models.UserInfo;
import com.example.models.Users;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<Object> createUser(@RequestBody(required = false) Users users) {
        if(ObjectUtils.isEmpty(users)){
            throw new ParameterMissingException("User body is missing");
        }
        if (ObjectUtils.isEmpty(users.getType())) {
            logger.error("User type is missing");
            throw new DataNotFoundException("User type is missing");
        }
        logger.info("Check the type of user");
        if (users.getType().equalsIgnoreCase("Hotel")) {
            logger.info("User type is hotel");
            logger.info("Check email,isAdmin,username is given or not");
            if (ObjectUtils.isEmpty(users.getEmail()) || ObjectUtils.isEmpty(users.isIsadmin()) || ObjectUtils.isEmpty(users.getUsername())) {
                logger.error("Some fields are misssing");
                throw new DataNotFoundException("Some fields are missing");
            }
            boolean isUserExist = userService.isUserAlreadyExist(users.getEmail());
            if (isUserExist) {
                logger.warn("User alreaedy exist for same email id");
                throw new DataAlreadyExistException("User Already Exist");
            } else {
                logger.info("Generate random and unique usertoken");
                String userToken = UUID.randomUUID().toString();
                users.setUsertoken(userToken);
                userService.save(users, userToken);
                logger.info("User data is saved to users database");
                return new ResponseEntity<>("Hello " + users.getUsername() + "\nUser Token : " + users.getUsertoken(),HttpStatus.OK);
            }
        } else {
            logger.error("for type" + users.getType() + "no need to sign up");
            throw new SignUpNotRequiredException("No need to sign up");
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<Object> signIn(@RequestBody(required = false) Users users) throws GenericException {
        if(ObjectUtils.isEmpty(users)){
            throw new ParameterMissingException("User body is missing");
        }
        if (ObjectUtils.isEmpty(users.getEmail()) && ObjectUtils.isEmpty(users.getUsername())) {
            logger.error("email and username both are missing");
            throw new DataNotFoundException("Email and Username both are missing");
        }
        logger.info("check for password is given or not");
        if(ObjectUtils.isEmpty(users.getPassword())){
            logger.error("password is missing");
            throw new DataNotFoundException("Password is Empty");
        }
        if (ObjectUtils.isEmpty(users.getEmail())) {
            logger.info("username is present for sign in");
            users = userService.getByUsernameAndPassword(users.getUsername(), users.getPassword());
            if (users == null) {
                logger.error("User does not exist");
                throw new DataNotFoundException("User does not exist");
            }
            if (users.getType().equalsIgnoreCase("Hotel")) {
                logger.info("User is of type Hotel need to provide email and password for sign in");
                throw new WrongInformationException("User type hotel need to give email and password for sign in");
            }
            if (!ObjectUtils.isEmpty(users.getUsertoken())) {
                logger.info("User successully signin");
                return new ResponseEntity<>(users.getUsertoken(),HttpStatus.OK);
            } else {
                logger.info("random and unique usertoken is generated");
                String userToken = UUID.randomUUID().toString();
                users.setUsertoken(userToken);
                userService.save(users, userToken);
                logger.info("User successfully signed in");
                return new ResponseEntity<>(userToken,HttpStatus.OK);
            }
        }
        if (ObjectUtils.isEmpty(users.getUsername())) {
            logger.info("email and password is given for sign in");
            users = userService.getUserByEmailAndPassword(users.getEmail(),users.getPassword());
            if (users == null) {
                logger.error("Email or password is incorrect");
                throw new DataNotFoundException("Email or Password is incorrect");
            }
            if (!users.getType().equalsIgnoreCase("Hotel")) {
                logger.error("User is of type" + users.getType() + " need to give username and password");
                throw new WrongInformationException("User type " + users.getType() + " need to give username and password for sign in");
            }
            if (!ObjectUtils.isEmpty(users.getUsertoken())) {
                logger.info("User successfully signed in");
                return new ResponseEntity<>(users.getUsertoken(),HttpStatus.OK);
            } else {
                logger.info("random and unique usertoken is generated");
                String userToken = UUID.randomUUID().toString();
                users.setUsertoken(userToken);
                System.out.println(users.toString());
                userService.save(users, userToken);
                logger.info("User successfully signed in");
                return new ResponseEntity<>(userToken,HttpStatus.OK);
            }
        } else {
            logger.error("Some information is extra");
            throw new GenericException("Some information is missing");
        }
    }

    
    @GetMapping("/userInfo")  // Done
    public ResponseEntity<Object> userInfo(@RequestHeader(name = "user_token",required = false) String userToken){
        logger.info("Userinfo api is running");
        System.out.println("in user info");
        Users users = userService.getUserInfo(userToken);
        UserInfo userInfo = new UserInfo(users.getEmail(), users.getUsername(), users.isIsadmin());
        logger.info("User info api is successfully run");
        return new ResponseEntity<>(userInfo,HttpStatus.OK);

    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<Object> deleteUser(@RequestHeader(name = "user_token",required = false) String userToken) throws GenericException {
        try {
            userService.deleteUser(userToken);
            logger.info("Delete user is successful");
            return new ResponseEntity<>("Delete Successfully",HttpStatus.OK);
        }catch (Exception e){
            logger.error("Delete user unsuccessful");
            throw new GenericException("Delete Unsuccessful");
        }
    }

    @DeleteMapping("/deleteUserByAdmin")
    public ResponseEntity<Object> deleteUserByAdmin(@RequestHeader(name = "user_token",required = false) String userToken,@RequestParam(name = "email",required = false) String email) throws GenericException {
        if(ObjectUtils.isEmpty(email)){
            throw new ParameterMissingException("Email is missing");
        }
        String userToken1 = userService.getUserTokenByEmail(email);
        if (userToken1 == null) {
            logger.error("User does not exist for given email so not possible to delete the user");
            throw new DataNotFoundException("User does not exist");
        }
        try {
            userService.deleteUser(userToken1);
            logger.info("Delete user is successful");
            return new ResponseEntity<>("Delete Successfully",HttpStatus.OK);
        }catch (Exception e){
            logger.error("Delete user is Unsuccessful");
            throw new GenericException("Delete Unsuccessful");
        }

    }

    @DeleteMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader(name = "user_token",required = false) String userToken) throws GenericException {
        try {
            userService.logout(userToken);
            logger.info("Logout is Successful");
            return new ResponseEntity<>("Logout Successful",HttpStatus.OK);
        }catch (Exception e){
            logger.error("Logout Unsuccessful");
            throw new GenericException("Logout Unsuccessful");
        }
    }
}
