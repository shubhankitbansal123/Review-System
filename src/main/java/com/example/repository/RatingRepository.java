package com.example.repository;

import com.example.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer>, PagingAndSortingRepository<Rating,Integer> {

}