package com.example.repository;

import com.example.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HotelRepository extends JpaRepository<Hotel,Integer>, PagingAndSortingRepository<Hotel,Integer> {

    @Query(value = "select case when exists(select * from hotel where hotelid = ?1) then true else false end",nativeQuery = true)
    boolean getHotelCount(Integer id);

    @Query(value = "select * from hotel where hotelname=?1 and location=?2",nativeQuery = true)
    Hotel fetchHotelByNameAndLocation(String hotelName, String location);


    @Query(value = "update hotel set averagerating=?2,noofpeople=noofpeople+1,rateaveragehotel=json_build_object('food',?3,'service',?4,'locality',?5,'hygine',?6,'security',?7) where hotelid=?1 returning true",nativeQuery = true)
    boolean updateAverageRating(Integer id,float average,float food,float service,float loyality,float hygine,float security);

    @Query(value = "select * from hotel where hotelid=?1",nativeQuery = true)
    Hotel fetchHotel(Integer typeid);

}
