package com.example.repository;

import com.example.models.Rating;
import com.example.models.RatingAverage;
import com.example.service.RatingService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {


    @Query(value = "delete from rating where type_id=?1",nativeQuery = true)
    void deleteDataOfHotel(Integer id);


    @Query(value = "with sourcedata as (select type_id,jsondata.key as key,avg(cast(jsondata.value as int)) as avg from rating,jsonb_each_text(rate) as jsondata group by type_id,jsondata.key) select type_id,json_object_agg(key,avg) as average from sourcedata group by type_id",nativeQuery = true)
    RatingAverage ratingAverage();
}
