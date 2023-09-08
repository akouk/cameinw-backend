package com.cameinw.cameinwbackend.user.service;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.request.ReviewRequest;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> getAllReviews();
    Review createReview(Integer placeId, ReviewRequest reviewRequest);
    Review updateReview(Integer userId, Integer reviewId, ReviewRequest reviewRequest);
    void deleteReview(Integer userId, Integer reviewId);
    List<Review> getReviewsByUserId(Integer userId);
    Review getReviewByUserId(Integer userId, Integer reviewId);
    List<Review> getReviewsByPlaceId(Integer placeId);
    Review getReviewByPlaceId(Integer placeId, Integer reviewId);
}
