package com.example.repository;

import com.example.models.Rating;
import com.example.models.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {
}
