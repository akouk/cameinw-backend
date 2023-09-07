package com.cameinw.cameinwbackend.user.service;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.request.ReviewRequest;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> getAllReviews();
    Review createReview(ReviewRequest reviewRequest);
    Review getReviewByReviewId(Integer reviewId);
    Review updateReview(Integer reviewId, ReviewRequest reviewRequest);
    void deleteReview(Integer reviewId);
    User getUserOfReview(Integer reviewId);
    Place getPlaceOfReview(Integer reviewId);


    Optional<List<Review>> getAllReviewsForPlace(Integer placeId);
}
