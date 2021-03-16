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


    @Query(value = "update hotel set averagerating=?2,noofpeople=noofpeople+1,rating=json_build_object('food',?3,'service',?4,'locality',?5,'hygine',?6,'security',?7) where hotelid=?1 returning true",nativeQuery = true)
    boolean updateAverageRating(Integer id,Double average,Double food,Double service,Double loyality,Double hygine,Double security);

    @Query(value = "select * from hotel where hotelid=?1 and hotelname=?2",nativeQuery = true)
    Hotel fetchHotel(Integer typeid,String name);

    @Query(value = "select * from hotel where hotelname ilike ?1 order by averagerating desc",nativeQuery = true)
    List<Hotel> fetchHotelByNameRegex(String value);

    @Query(value = "select * from hotel where location ilike ?1 order by averagerating desc",nativeQuery = true)
    List<Hotel> fetchHotelByLocationRegex(String value);

    @Query(value = "select case when exists(select * from hotel where hotelid = ?1 and hotelname=?2) then true else false end",nativeQuery = true)
    boolean getHotelCountWithName(Integer typeid, String name);
}
