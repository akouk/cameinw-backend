package com.cameinw.cameinwbackend.place.service_impl;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.repository.ReviewRepository;
import com.cameinw.cameinwbackend.place.service.ReviewService;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImplementation implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public ReviewServiceImplementation(ReviewRepository reviewRepository, UserRepository userRepository, PlaceRepository placeRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    @Transactional
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Optional<Review> getReviewById(Integer reviewId) {
        return reviewRepository.findById(reviewId);
    }

    @Override
    @Transactional
    public Review updateReview(Integer reviewId, Review updatedReview) {
        Optional<Review> optionalExistingReview = reviewRepository.findById(reviewId);
        if (optionalExistingReview.isPresent()) {
            Review existingReview = optionalExistingReview.get();
            existingReview.setScore(updatedReview.getScore());
            existingReview.setComment(updatedReview.getComment());
            return reviewRepository.save(existingReview);
        } else {
            throw new ResourceNotFoundException("Review with ID " + reviewId + " not found.");
        }
    }

    @Override
    @Transactional
    public void deleteReview(Integer reviewId) {
        Optional<Review> optionalExistingReview = reviewRepository.findById(reviewId);
        if (optionalExistingReview.isPresent()) {
            reviewRepository.deleteById(reviewId);
        } else {
            throw new ResourceNotFoundException("Review with ID " + reviewId + " not found.");
        }
    }

    @Override
    public Optional<User> getUserForReview(Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .map(Review::getUser);
    }

    @Override
    public Optional<Place> getPlaceForReview(Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .map(Review::getPlace);
    }
    @Override
    public Optional<List<Review>> getAllReviewsByUser(Integer userId) {
        return userRepository.findById(userId)
                .map(User::getReviews);
    }

    @Override
    public Optional<List<Review>> getAllReviewsForPlace(Integer placeId) {
        return placeRepository.findById(placeId)
                .map(Place::getReviews);
    }
}
