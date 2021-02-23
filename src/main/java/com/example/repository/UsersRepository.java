package com.example.repository;

import com.example.models.Rating;
import com.example.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersRepository extends JpaRepository<Users,String> {

    @Query(value = "select count(*) from users where email=?1",nativeQuery = true)
    public Integer userAlreadyExist(String email);

    @Query(value = "select count(*) from users where email=?1",nativeQuery = true)
    public Integer legitUser(String email);

    @Query(value = "select access_token from users where email=?1",nativeQuery = true)
    public String getAccessToken(String email);

    @Query(value = "select count(*) from users where access_token=?1",nativeQuery = true)
    public Integer checkAccessToken(String accessToken);

    @Query(value = "select * from users where access_token=?1",nativeQuery = true)
    public Users getUserInfo(String accesstoken);

    @Query(value = "delete from users where access_token=:access_token",nativeQuery = true)
    void deleteUser(@Param("access_token") String access_token);

    @Query(value = "select is_admin from users where access_token=?1",nativeQuery = true)
    public boolean getUserType(String accessToken);

    @Query(value = "delete from users where email=?1",nativeQuery = true)
    public void deleteUserByEmail(String email);

    @Query(value = "select count(*) from users where access_token=?1 and user_id=?2",nativeQuery = true)
    public Integer checkUser(String accessToken,Integer user_id);

    @Query(value = "select user_id from users where access_token=?1",nativeQuery = true)
    public Integer getUserIdFromAccessToken(String accessToken);

    @Query(value = "select count(*) from users where access_token=?1 and is_admin=1",nativeQuery = true)
    public Integer checkAdmin(String accessToken);
}
