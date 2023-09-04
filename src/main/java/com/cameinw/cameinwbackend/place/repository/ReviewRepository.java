package com.cameinw.cameinwbackend.place.repository;

import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.user.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{
    Optional<Review> findById(Integer reviewId);

}
