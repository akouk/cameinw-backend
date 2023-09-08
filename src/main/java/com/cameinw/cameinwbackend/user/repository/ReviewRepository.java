package com.cameinw.cameinwbackend.user.repository;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{
    Optional<Review> findById(Integer reviewId);
    List<Review> findByUser(User user);
    List<Review> findByPlace(Place place);
    @Query(value = "SELECT r FROM Review r WHERE r.user = :user AND r.place = :place")
    List<Review> findByUserAndPlace(@Param("user") User user, @Param("place") Place place);

}
