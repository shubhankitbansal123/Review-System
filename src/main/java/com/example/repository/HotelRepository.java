package com.example.repository;

import com.example.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HotelRepository extends JpaRepository<Hotel,Integer>, PagingAndSortingRepository<Hotel,Integer> {

    @Query(value = "select case when exists(select * from hotel where id = ?1) then true else false end",nativeQuery = true)
    boolean getHotelCount(Integer id);

    @Query(value = "delete from hotel where id=?1 returning true",nativeQuery = true)
    boolean deleteHotel(Integer id);

    @Query(value = "select * from hotel where hotel_name=?1 and location=?2",nativeQuery = true)
    Hotel fetchHotelByNameAndLocation(String hotelName, String location);
}
