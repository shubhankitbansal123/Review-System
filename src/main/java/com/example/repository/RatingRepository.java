package com.example.repository;

import com.example.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer>, PagingAndSortingRepository<Rating,Integer> {

    @Query(value = "select case when exists(select * from rating where typeid=?1 and type='Hotel') then true else false end",nativeQuery = true)
    boolean checkByHotelTypeId(Integer id);

    @Query(value = "delete from rating where typeid=?1 and type='Hotel' returning true",nativeQuery = true)
    boolean deleteByHotelTypeId(Integer id);
}