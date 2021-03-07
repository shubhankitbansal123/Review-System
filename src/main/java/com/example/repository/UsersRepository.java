package com.example.repository;

import com.example.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersRepository extends JpaRepository<Users,String> {

    @Query(value = "select case when exists(select * from users where email=?1) then true else false end",nativeQuery = true)
    boolean userAlreadyExist(String email);

    @Query(value = "select * from users where user_token=?1",nativeQuery = true)
    Users getUserInfo(String userToken);

    @Query(value= "update users set user_token=null where user_token=?1 returning true",nativeQuery = true)
    boolean logout(String userToken);

    @Query(value = "select * from users where email=?1",nativeQuery = true)
    Users getUser(String email);

    @Query(value = "delete from users where user_token=?1 returning *",nativeQuery = true)
    Users deleteUser(String userToken);

    @Query(value = "select user_token from users where email=?1",nativeQuery = true)
    String getUserTokenByEmail(String email);

    @Query(value = "select * from users where username=?1 and password=?2",nativeQuery = true)
    Users getByUsernameAndPassword(String username, String password);
}
