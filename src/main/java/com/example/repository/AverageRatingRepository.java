package com.example.repository;

import com.example.models.AverageRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AverageRatingRepository extends JpaRepository<AverageRating,Integer> {

    @Query(value ="update averagerating set averagerate=?2,numberofpeople=numberofpeople+1,rateaverageinventory=json_build_object('a',?3,'b',?4,'c',?5,'d',?6,'e',?7) where inventoryid=?1 returning true" ,nativeQuery = true)
    boolean updateRatingInventory(Integer inventoryid,float average, float a,float b,float c,float d,float e);

    @Query(value ="update averagerating set averagerate=?2,numberofpeople=numberofpeople+1,rateaverageott=json_build_object('a',?3,'b',?4,'c',?5,'d',?6,'e',?7) where ottid=?1 returning true" ,nativeQuery = true)
    boolean updateRatingOtt(Integer ottid,float average,float a,float b, float c,float d,float e);

    Page<AverageRating> findAllByType(String type, Pageable pageable);

    @Query(value = "select * from averagerating where ottid=?1",nativeQuery = true)
    AverageRating getAverageRatingForOtt(Integer ottid);

    @Query(value = "select * from averagerating where inventoryid=?1",nativeQuery = true)
    AverageRating getAverageRatingForInventory(Integer inventoryid);
}
