package com.cameinw.cameinwbackend.user.controller;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.projection.PlaceProjection;
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

   // @Autowired
    //private ReviewService reviewService;

    @GetMapping() // ---- check ok -----
    public List<User> getAllUsers() { // ---- check ok -----
        return userService.getAllUsers();
    }

    @GetMapping("/{user_id}") // ---- check ok -----
    public ResponseEntity<User> getUserById(@PathVariable("user_id") Integer userId) {
        Optional<User> user = userService.getUserByUserId(userId);
        return user
                .map(ResponseEntity::ok) // STATUS: 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // STATUS: 404 Not Found
    }


    @PutMapping("/{user_id}") // ---- check ok -----
    public ResponseEntity<String> updateUser(@PathVariable("user_id") Integer userId, @RequestBody User updatedUser) {
        try {
            User updated = userService.updateUser(userId, updatedUser);
            if (updated != null) {
                return ResponseEntity.ok("User updated successfully"); // STATUS: 200 OK
            } else {
                return ResponseEntity.notFound().build(); // STATUS: 404 Not Found
            }
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); // STATUS: 404 Not Found
        }
    }

    @DeleteMapping("/{user_id}") // ---- check ok -----
    public ResponseEntity<?> deleteUser(@PathVariable("user_id") Integer userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully."); // STATUS: 204 No Content
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); // STATUS: 404 Not Found
        }
    }

    @GetMapping("/{user_id}/places")
    public ResponseEntity<?> getPlacesByUserId(@PathVariable("user_id") Integer userId) {
        try {
            List<PlaceProjection> places = userService.getPlacesByUserId(userId).orElse(Collections.emptyList());
            return ResponseEntity.ok(places); // STATUS: 200 OK
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); // STATUS: 404 Not Found
        }
    }

//    @GetMapping("/{user_id}/reviews") //CHECK OK
//    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Integer user_id) {
//        Optional<List<Review>> reviews = reviewService.getAllReviewsByUser(user_id);
//        return reviews
//                .map(ResponseEntity::ok) // STATUS: 200 OK
//                .orElseGet(() -> ResponseEntity.notFound().build()); // STATUS: 404 Not Found
//    }
}
