package com.example.repository;

import com.example.models.Comment;
import com.example.models.Rating;
import com.example.models.RatingAverage;
import com.example.service.RatingService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {


    @Query(value = "delete from rating where type_id=?1 returning true",nativeQuery = true)
    boolean deleteDataOfHotel(Integer id);


    @Query(value = "select * from rating order by (type,type_id)",nativeQuery = true)
    List<Rating> ratingAverage();

    @Query(value = "select case when exists(select * from rating where type_id=?1) then true else false end",nativeQuery = true)
    boolean getRatingCount(Integer id);
}