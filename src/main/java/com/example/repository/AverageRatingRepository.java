package com.example.repository;

import com.example.models.AverageRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AverageRatingRepository extends JpaRepository<AverageRating,Integer> {

    @Query(value ="update averagerating set averagerate=?2,numberofpeople=numberofpeople+1,rating=json_build_object('quality',?3,'asAdvertised',?4,'satisfaction') where typeid=?1 and type='Inventory' returning true" ,nativeQuery = true)
    boolean updateRatingInventory(Integer inventoryid,Double average, Double quality,Double asAdvertised,Double satisfaction);

    @Query(value ="update averagerating set averagerate=?2,numberofpeople=numberofpeople+1,rating=json_build_object('a',?3,'b',?4,'c',?5,'d',?6,'e',?7) where typeid=?1 and type='Ott' returning true" ,nativeQuery = true)
    boolean updateRatingOtt(Integer ottid,Double average,Double a,Double b, Double c,Double d,Double e);

    Page<AverageRating> findAllByType(String type, Pageable pageable);

    @Query(value = "select * from averagerating where typeid=?1 and type='Ott'",nativeQuery = true)
    AverageRating getAverageRatingForOtt(Integer ottid);

    @Query(value = "select * from averagerating where typeid=?1 and type='Inventory'",nativeQuery = true)
    AverageRating getAverageRatingForInventory(Integer inventoryid);
}
