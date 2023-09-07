package com.cameinw.cameinwbackend.user.service;

import com.cameinw.cameinwbackend.place.projection.PlaceProjection;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User getUserByUserId(Integer userId);
    User updateUser(Integer userId, User updatedUser);
    void deleteUser(Integer userId);
    Optional<List<PlaceProjection>> getPlacesByUserId(Integer userId);
    List<Reservation> getReservationsByUserId (Integer userId);
    List<Review> getReviewsByUserId(Integer userId);


}
