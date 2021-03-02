package com.example.repository;

import com.example.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface UsersRepository extends JpaRepository<Users,String> {

    @Query(value = "select case when exists(select * from users where email=?1) then true else false end",nativeQuery = true)
    boolean userAlreadyExist(String email);

    @Query(value = "select case when exists(select * from users where email=?1) then true else false end",nativeQuery = true)
    boolean legitUser(String email);

    @Query(value = "select user_token from users where email=?1",nativeQuery = true)
    String getUserToken(String email);

    @Query(value = "select case when exists(select * from users where user_token=?1) then true else false end",nativeQuery = true)
    boolean checkUserToken(String accessToken);

    @Query(value = "select * from users where user_token=?1",nativeQuery = true)
    Users getUserInfo(String userToken);

    @Query(value = "select is_admin from users where user_token=?1",nativeQuery = true)
    boolean getUserType(String userToken);

    @Query(value = "delete from users where email=?1 returning true",nativeQuery = true)
    boolean deleteUserByEmail(String email);

    @Query(value = "select case when exists(select count(*) from users where user_token=?1 and user_id=?2 and type=?3) then true else false end",nativeQuery = true)
    boolean checkUser(String userToken,Integer user_id,String type);

    @Query(value = "select user_id from users where user_token=?1",nativeQuery = true)
    Integer getUserIdFromUserToken(String userToken);

    @Query(value = "select * from users where userToken=?1 and is_admin=1",nativeQuery = true)
    Users checkAdmin(String userToken);

    @Query(value = "update users set user_token=?2 where email=?1 returning true",nativeQuery = true)
    boolean setAccessToken(String email, String userToken);

    @Query(value= "update users set user_token=null where user_token=?1 returning true",nativeQuery = true)
    boolean logout(String userToken);

    @Query(value = "select * from users where email=?1",nativeQuery = true)
    Users getUser(String email);

    @Query(value = "select is_admin from users where email=?1",nativeQuery = true)
    boolean checkAdminUsingEmail(String email);

    @Query(value = "delete from users where user_token=?1 returning *",nativeQuery = true)
    Users deleteUser(String userToken);

    @Query(value = "select user_token from users where email=?1",nativeQuery = true)
    String getUserTokenByEmail(String email);
}
