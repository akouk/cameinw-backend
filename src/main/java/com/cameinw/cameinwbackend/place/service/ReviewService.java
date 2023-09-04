package com.cameinw.cameinwbackend.place.service;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> getAllReviews();

    Review createReview(Review review);

    Optional<Review> getReviewById(Integer reviewId);

    Review updateReview(Integer reviewId, Review updatedReview);

    void deleteReview(Integer reviewId);

    Optional<User> getUserForReview(Integer reviewId);

    Optional<Place> getPlaceForReview(Integer reviewId);

    Optional<List<Review>> getAllReviewsByUser(Integer userId);

    Optional<List<Review>> getAllReviewsForPlace(Integer placeId);
}
