package com.example.repository;

import com.example.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface HotelRepository extends JpaRepository<Hotel,Integer> {

    @Query(value = "select count(*) from hotel where id = ?1",nativeQuery = true)
    public Integer getHotelCount(Integer id);

    @Query(value = "delete from hotel where id=?1",nativeQuery = true)
    public void deleteHotel(Integer id);

    @Query(value = "select count(*) from hotel where hotel_id=?1",nativeQuery = true)
    public Integer chechHotel(Integer hotel_id);
}
