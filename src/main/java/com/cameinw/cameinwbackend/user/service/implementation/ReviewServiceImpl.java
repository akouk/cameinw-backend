package com.cameinw.cameinwbackend.user.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.ReservationRepository;
import com.cameinw.cameinwbackend.user.repository.ReviewRepository;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.user.request.ReviewRequest;
import com.cameinw.cameinwbackend.user.service.ReviewService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

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

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews were found.");
        }
        return reviews;
    }

    @Override
    @Transactional
    public Review createReview(ReviewRequest reviewRequest) {
        // Retrieve the user and place associated with the review
        User user = reviewRequest.getUser();
        getUserById(user.getId());
        Place place = reviewRequest.getPlace();
        getPlaceById(place.getId());

        // Check if the user has made a reservation for the specified place
        //boolean hasReservation = userHasReservationForPlace(user, place);

        if (userHasReservationForPlace(user, place)) {
            // The user has a reservation for the place, so save the review
            Review newReview = createNewReview(reviewRequest, user, place);
            return saveReview(newReview);
        } else {
            throw new CustomUserFriendlyException("User has not made a reservation for this place.");
        }
    }

    @Override
    public Review getReviewByReviewId(Integer reviewId) {
        return getReviewById(reviewId);
    }

    @Override
    @Transactional
    public Review updateReview(Integer reviewId, ReviewRequest reviewRequest) {
        User user = reviewRequest.getUser();
        getUserById(user.getId());
        Place place = reviewRequest.getPlace();
        getPlaceById(place.getId());
        Review updateReview = getReviewById(reviewId);

        if (userHasReservationForPlace(user, place)) {
            updateReview.setScore(reviewRequest.getScore());
            updateReview.setComment(reviewRequest.getComment());
            return saveReview(updateReview);
        } else {
            throw new CustomUserFriendlyException("User has not made a reservation for this place.");
        }
    }

    @Override
    @Transactional
    public void deleteReview(Integer reviewId) {
        Review review = getReviewById(reviewId);
        reviewRepository.delete(review);
    }

    @Override
    public User getUserOfReview(Integer reviewId) {
        Review review = getReviewById(reviewId);
        return review.getUser();
    }

    @Override
    public Place getPlaceOfReview(Integer reviewId) {
        Review review = getReviewById(reviewId);
        return review.getPlace();
    }

    @Override
    public Optional<List<Review>> getAllReviewsForPlace(Integer placeId) {
        return placeRepository.findById(placeId)
                .map(Place::getReviews);
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

    private Review createNewReview(ReviewRequest reviewRequest, User user, Place place) {
        return Review.builder()
                .score(reviewRequest.getScore())
                .comment(reviewRequest.getComment())
                .user(user)
                .place(place).build();
    }

    private Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

}
