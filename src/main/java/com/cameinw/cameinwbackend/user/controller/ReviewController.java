package com.cameinw.cameinwbackend.user.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.response.GenericResponse;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.request.ReviewRequest;
import com.cameinw.cameinwbackend.user.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/reviews") // CHECK OK!!
    public ResponseEntity<?>  getAllReviews() {
        try {
            List<Review> reviews = reviewService.getAllReviews();
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/places/{place_id}/reviews") // !!CHECK OK!!
    public ResponseEntity<Object> createReview(
            @PathVariable("place_id") Integer placeId,
            @Valid  @RequestBody ReviewRequest reviewRequest) {
        try {
            Review createReview = reviewService.createReview(placeId, reviewRequest);
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage("Review successfully created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/users/{user_id}/reviews/{review_id}")
    public ResponseEntity<Object> updateReview(
            @PathVariable("user_id") Integer userId,
            @PathVariable("review_id") Integer reviewId,
            @RequestBody ReviewRequest reviewRequest) {
        try {
            Review updateReview = reviewService.updateReview(userId, reviewId, reviewRequest);
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage("Review successfully updated.");
            return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // Only the user who made the review can delete it!
    @DeleteMapping("/users/{user_id}/reviews/{review_id}")
    public ResponseEntity<Object> deleteReview(
            @PathVariable("user_id") Integer userId,
            @PathVariable("review_id") Integer reviewId) {
        try {
            reviewService.deleteReview(userId, reviewId);
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage("Review deleted successfully.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/users/{user_id}/reviews")  // -----------
    public ResponseEntity<?> getReviewsByUserId(@PathVariable("user_id") Integer userId) {
        try {
            List<Review> reviews = reviewService.getReviewsByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(reviews);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/users/{user_id}/reviews/{review_id}")  // -----------
    public ResponseEntity<?> getReviewByUserId(
            @PathVariable("user_id") Integer userId,
            @PathVariable("review_id") Integer reviewId) {
        try {
            Review review = reviewService.getReviewByUserId(userId, reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(review);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


    @GetMapping("/places/{place_id}/reviews")  // !!CHECK OK!!
    public ResponseEntity<?> getReviewsByPlaceId(@PathVariable("place_id") Integer placeId) {
        try {
            List<Review> reviews = reviewService.getReviewsByPlaceId(placeId);
            return ResponseEntity.status(HttpStatus.OK).body(reviews);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/places/{place_id}/reviews/{review_id}") // !!CHECK OK!!
    public ResponseEntity<?> getReviewByPlaceId(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("review_id") Integer reviewId) {
        try {
            Review review = reviewService.getReviewByPlaceId(placeId, reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(review);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
