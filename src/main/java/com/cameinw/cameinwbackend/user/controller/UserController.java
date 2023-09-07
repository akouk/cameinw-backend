package com.cameinw.cameinwbackend.user.controller;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.projection.PlaceProjection;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping() // !!CHECK OK!!
    public ResponseEntity<?> getAllUsers() { // ---- check ok -----
        try {
            List<User> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{user_id}") // !!CHECK OK!!
    public ResponseEntity<?> getUserById(@PathVariable("user_id") Integer userId) {
        try {
            return new ResponseEntity<>(userService.getUserByUserId(userId), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


    @PutMapping("/{user_id}") // !!CHECK OK!!
    public ResponseEntity<String> updateUser(@PathVariable("user_id") Integer userId, @RequestBody User updatedUser) {
        try {
            User updated = userService.updateUser(userId, updatedUser);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User successfully updated");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{user_id}") // !!CHECK OK!!
    public ResponseEntity<?> deleteUser(@PathVariable("user_id") Integer userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{user_id}/places") // !!CHECK OK!!
    public ResponseEntity<?> getPlacesByUserId(@PathVariable("user_id") Integer userId) {
        try {
            List<PlaceProjection> places = userService.getPlacesByUserId(userId).orElse(Collections.emptyList());
            return new ResponseEntity<>(places, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{user_id}/reservations") //
    public ResponseEntity<?> getReservationsByUserId(@PathVariable("user_id") Integer userId) {
        try {
            List<Reservation> reservations = userService.getReservationsByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(reservations);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{user_id}/reviews")  //
    public ResponseEntity<?> getReviewsByUserId(@PathVariable("user_id") Integer userId) {
        try {
            List<Review> reviews = userService.getReviewsByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(reviews);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
