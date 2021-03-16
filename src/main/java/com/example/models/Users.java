package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")
@IdClass(UsersId.class)
@RedisHash("Users")
public class Users implements Serializable {
    @Id
    @Column(name = "userid",nullable = false)
    @JsonProperty("userid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;
    @Column(name = "email",unique = true)
    @JsonProperty("email")
    private String email;
    @Column(name = "username",unique = true)
    @JsonProperty("username")
    private String username;
    @Column(name = "isadmin")
    @JsonProperty("isadmin")
    private boolean isadmin;
    @Column(name = "usertoken",unique = true)
    @JsonProperty("usertoken")
    private String usertoken;
    @Id
    @Column(name = "type")
    @JsonProperty("type")
    private String type;
    @JsonProperty("password")
    @Column(name = "password")
    private String password;

}
