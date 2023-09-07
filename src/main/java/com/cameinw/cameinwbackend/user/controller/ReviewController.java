package com.cameinw.cameinwbackend.user.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.model.Regulation;
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
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping() //CHECK OK!!
    public ResponseEntity<?>  getAllReviews() {
        try {
            List<Review> reviews = reviewService.getAllReviews();
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<String> createReview(@Valid  @RequestBody ReviewRequest reviewRequest) {
        try {
            Review createReview = reviewService.createReview(reviewRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Review successfully created.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/{review_id}") //CHECK OK!!
    public ResponseEntity<?> getReviewById(@PathVariable("review_id") Integer reviewId) {
        try {
            Review review = reviewService.getReviewByReviewId((reviewId));
            return ResponseEntity.status(HttpStatus.OK).body(review);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/{review_id}") //CHECK OK
    public ResponseEntity<String> updateReview(@PathVariable("review_id") Integer reviewId, @Valid @RequestBody ReviewRequest reviewRequest) {
        try {
            Review updateReview = reviewService.updateReview(reviewId, reviewRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Review successfully updated.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{review_id}") //CHECK OK
    public ResponseEntity<?> deleteReview(@PathVariable("review_id") Integer reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Review deleted successfully.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{review_id}/user") //CHECK OK
    public ResponseEntity<?> getUserOfReview(@PathVariable("review_id") Integer reviewId) {
        try {
            User user = reviewService.getUserOfReview(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(user);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{review_id}/place") //CHECK OK
    public ResponseEntity<?> getPlaceOfReview(@PathVariable("review_id") Integer reviewId) {
        try {
            Place place = reviewService.getPlaceOfReview(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(place);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
