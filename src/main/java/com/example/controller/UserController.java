package com.example.controller;

import com.example.models.UserInfo;
import com.example.models.Users;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping("/signUp") // Everybody access and create user with unique emailId
    public String createUser(@RequestBody Users users){

        if(StringUtils.isEmpty(users.getEmail())) {
            return "Email is required";
        }
        if(StringUtils.isEmpty(users.getType())){
            return "Type is required";
        }
        if(!users.getType().equalsIgnoreCase("Hotel") && StringUtils.isEmpty(users.getPassword())){
            return "For " +users.getType()+" password is necessary";
        }
        Integer isUserExist = usersRepository.userAlreadyExist(users.getEmail());
        if(isUserExist==1){
            return "User Already Exist";
        }
        else {
            String userToken = UUID.randomUUID().toString();
            users.setAccess_token(userToken);
            usersRepository.save(users);
            return "Hello " + users.getUsername() + "\nUser Token : " + users.getAccess_token();
        }
    }

    @GetMapping("/signIn")  // Everybody signIn with correct emailId and password
    public String signIn(@RequestBody Users users){
        Integer islegitUser = usersRepository.legitUser(users.getEmail());
        if(islegitUser==0){
            return "User does not Exist";
        }
        else {
            String getToken = usersRepository.getAccessToken(users.getEmail());
            return getToken;
        }
    }
    
    @GetMapping("/userInfo")
    public UserInfo userInfo(@RequestHeader("access_token") String accessToken){
        System.out.println("userInfo");
        Users users = usersRepository.getUserInfo(accessToken);
        UserInfo userInfo = new UserInfo(users.getEmail(),users.getUsername(),users.is_admin());
        return userInfo;
    }

    @DeleteMapping("/deleteUser")     // it gives an error
    public String deleteUser(@RequestHeader("access_token") String accessToken){
        System.out.println("deleteUser");
        usersRepository.deleteUser(accessToken);
        return "Delete Successfully";
    }

    @DeleteMapping("/deleteUserByAdmin/{email}")  // delete user by admin
    public String deleteUserByAdmin(@RequestHeader("access_token") String accessToken,@PathVariable String email){
        Integer count1 = usersRepository.legitUser(email);
        if(count1==0){
            return "user does not exist";
        }
        usersRepository.deleteUserByEmail(email); // work fine but shown give error for no results were returned by the query
        return null;
    }







}
