package com.cameinw.cameinwbackend.user.repository;

import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{
    Optional<Review> findById(Integer reviewId);
    List<Review> findByUser(User user);
}
