package com.example.repository;

import com.example.models.Users;
import com.example.models.UsersId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Id;
import java.util.Optional;


@Repository
public interface UsersRepository extends JpaRepository<Users, UsersId> {

    @Query(value = "select case when exists(select * from users where email=?1) then true else false end",nativeQuery = true)
    boolean userAlreadyExist(String email);

    @Query(value = "select * from users where usertoken=?1",nativeQuery = true)
    Users getUserInfo(String userToken);

    @Query(value= "update users set usertoken=null where usertoken=?1 returning true",nativeQuery = true)
    boolean logout(String userToken);

    @Query(value = "select * from users where email=?1 and password=?2",nativeQuery = true)
    Users getUserByEmailAndPassword(String email,String password);

    @Query(value = "delete from users where usertoken=?1 returning *",nativeQuery = true)
    Users deleteUser(String userToken);

    @Query(value = "select usertoken from users where email=?1",nativeQuery = true)
    String getUserTokenByEmail(String email);

    @Query(value = "select * from users where username=?1 and password=?2",nativeQuery = true)
    Users getByUsernameAndPassword(String username, String password);
}
