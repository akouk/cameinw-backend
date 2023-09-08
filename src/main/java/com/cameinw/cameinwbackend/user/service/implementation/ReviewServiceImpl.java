package com.cameinw.cameinwbackend.user.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.ReservationRepository;
import com.cameinw.cameinwbackend.user.repository.ReviewRepository;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.user.request.ReviewRequest;
import com.cameinw.cameinwbackend.user.service.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            PlaceRepository placeRepository,
            ReservationRepository reservationRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Review> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        checkIfReviewsWereFound(reviews);
        return reviews;
    }

    @Override
    @Transactional
    public Review createReview(Integer placeId, ReviewRequest reviewRequest) {
        Place place = getPlaceById(placeId);
        User user = getUserById(reviewRequest.getUser().getId());
        checkIfCanPostReview(user, place);
        checkIfUserHasAlreadyReviewedThePlace(user, place);

        Review newReview = createNewReview(reviewRequest, user, place);
        return saveReview(newReview);
    }

    @Override
    public List<Review> getReviewsByUserId(Integer userId) {
        User user = getUserById(userId);
        List<Review> reviews = reviewRepository.findByUser(user);
        checkIfReviewsWereFound(reviews);
        return reviews;
    }

    @Override
    public Review getReviewByUserId(Integer userId, Integer reviewId) {
        User user = getUserById(userId);
        Review review = getReviewById(reviewId);
        return review;
    }

    @Override
    public List<Review> getReviewsByPlaceId(Integer placeId) {
        Place place = getPlaceById(placeId);
        List<Review> reviews = reviewRepository.findByPlace(place);
        checkIfReviewsWereFound(reviews);
        return reviews;
    }

    @Override
    public Review getReviewByPlaceId(Integer placeId, Integer reviewId) {
        Place place = getPlaceById(placeId);
        Review review = getReviewById(reviewId);
        return review;
    }


    @Override
    @Transactional
    public Review updateReview(Integer userId, Integer reviewId, ReviewRequest reviewRequest) {
        Review review = getReviewById(reviewId);

        copyOnlyNonNullProperties(reviewRequest, review);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.flush();
        return saveReview(review);
    }

    @Override
    @Transactional
    public void deleteReview(Integer userId, Integer reviewId) {
        User user = getUserById(userId);
        Review review = getReviewById(reviewId);
        reviewRepository.delete(review);
    }

    private void checkIfReviewsWereFound(List<Review> reviews) {
        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews were found.");
        }
    }

    private Review getReviewById(Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found."));
    }
    private Place getPlaceById(Integer placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    private boolean userHasReservationForPlace(User user, Place place) {
        List<Reservation> reservations = reservationRepository.findByUserAndPlace(user, place);
        return !reservations.isEmpty();
    }

    private void checkIfCanPostReview(User user, Place place) {
        if (!userHasReservationForPlace(user, place)) {
           throw new CustomUserFriendlyException("User has not made a reservation for this place.");
        }
    }

    private boolean hasUserReviewedPlace(User user, Place place) {
        List<Review> existingReviews = reviewRepository.findByUserAndPlace(user, place);
        return !existingReviews.isEmpty();
    }

    private void checkIfUserHasAlreadyReviewedThePlace(User user, Place place) {
        if (hasUserReviewedPlace(user, place)) {
            throw new CustomUserFriendlyException("You have already reviewed this place. You can only update your review.");
        }
    }
    private Review createNewReview(ReviewRequest reviewRequest, User user, Place place) {
        reviewRequest.validate();
        LocalDateTime now = LocalDateTime.now();

        Review review = Review.builder()
                .rating(reviewRequest.getRating())
                .comment(reviewRequest.getComment())
                .user(user)
                .place(place)
                .build();

        review.setCreatedAt(now);
        review.setUpdatedAt(now);

        return review;
    }

    private void copyOnlyNonNullProperties(ReviewRequest reviewRequest, Review review) {
        LocalDateTime now = LocalDateTime.now();

        if (reviewRequest.getRating() != null) {
            review.setRating(reviewRequest.getRating());
        }
        if (reviewRequest.getComment() != null) {
            review.setComment(reviewRequest.getComment());
        }
    }

    private Review saveReview(Review review) {
        return reviewRepository.save(review);
    }
}
