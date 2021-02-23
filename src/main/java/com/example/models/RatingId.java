package com.example.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

public class RatingId implements Serializable {

    @Column(name = "user_id",nullable = false)
    private Integer user_id;

    @Column(name = "hotel_id",nullable = false)
    private Integer hotel_id;

    public RatingId() {
    }

    public RatingId(Integer user_id, Integer hotel_id) {
        this.user_id = user_id;
        this.hotel_id = hotel_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id,hotel_id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RatingId ratingId = (RatingId) obj;
        return user_id.equals(ratingId.user_id) &&
                hotel_id.equals(ratingId.hotel_id);
    }
}
