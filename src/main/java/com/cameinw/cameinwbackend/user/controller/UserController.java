package com.cameinw.cameinwbackend.user.controller;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.projection.PlaceProjection;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

   // @Autowired
    //private ReviewService reviewService;

    @GetMapping() //CHECK OK
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{user_id}") //CHECK OK
    public ResponseEntity<User> getUserById(@PathVariable("user_id") Integer userId) {
        Optional<User> user = userService.getUserById(userId);
        return user
                .map(ResponseEntity::ok) // STATUS: 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // STATUS: 404 Not Found
    }


    @PutMapping("/{user_id}")
    public ResponseEntity<User> updateUser(@PathVariable("user_id") Integer userId, @RequestBody User updatedUser) {
        try {
            User updated = userService.updateUser(userId, updatedUser);
            if (updated != null) {
                return ResponseEntity.ok(updated); // STATUS: 200 OK
            } else {
                return ResponseEntity.notFound().build(); // STATUS: 404 Not Found
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // STATUS: 404 Not Found
        }
    }

    @DeleteMapping("/{user_id}") //CHECK OK
    public ResponseEntity<?> deleteUser(@PathVariable("user_id") Integer userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully."); // STATUS: 204 No Content
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found."); // STATUS: 404 Not Found
        }
    }

    @GetMapping("/users/{user_id}/places")
    public ResponseEntity<List<PlaceProjection>> getPlacesByUser(@PathVariable("user_id") Integer userId) {

        return ResponseEntity.ok(userService.getPlacesByUserId(userId));
    }

//    @GetMapping("/{user_id}/reviews") //CHECK OK
//    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Integer user_id) {
//        Optional<List<Review>> reviews = reviewService.getAllReviewsByUser(user_id);
//        return reviews
//                .map(ResponseEntity::ok) // STATUS: 200 OK
//                .orElseGet(() -> ResponseEntity.notFound().build()); // STATUS: 404 Not Found
//    }
}
