package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",nullable = false)
    @JsonProperty("user_id")
    private Integer user_id;
    @Column(name = "email",unique = true,nullable = false)
    @JsonProperty("email")
    private String email;
    @Column(name = "username",nullable = false)
    @JsonProperty("username")
    private String username;
    @Column(name = "is_admin",nullable = false)
    @JsonProperty("is_admin")
    private boolean is_admin;
    @Column(name = "access_token",unique = true,nullable = false)
    @JsonProperty("access_token")
    private String access_token;
    @Column(name = "type",nullable = false)
    @JsonProperty("type")
    private String type;
    @JsonProperty("password")
    @Column(name = "password")
    private String password;


}
