package com.cameinw.cameinwbackend.user.controller;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.projection.PlaceProjection;
import com.cameinw.cameinwbackend.response.GenericResponse;
import com.cameinw.cameinwbackend.user.dto.UserDTO;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping() // !!CHECK OK!!
    public ResponseEntity<?> getAllUsers() { // ---- check ok -----
        GenericResponse genericResponse = new GenericResponse();

        try {
            List<User> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

//    @GetMapping("/{user_id}") // !!CHECK OK!!
//    public ResponseEntity<Object> getUserById(@PathVariable("user_id") Integer userId) {
//        GenericResponse genericResponse = new GenericResponse();
//        try {
//            return new ResponseEntity<>(userService.getUserByUserId(userId), HttpStatus.OK);
//        } catch (ResourceNotFoundException ex) {
//            genericResponse.setMessage(ex.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
//        }
//    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable("user_id") Integer userId) {
        try {
            User user = userService.getUserByUserId(userId); // Retrieve the User entity
            UserDTO userDTO = mapUserToDTO(user); // Map the User entity to UserDTO
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }


    @PutMapping("/{user_id}") // !!CHECK OK!!
    public ResponseEntity<Object> updateUser(@PathVariable("user_id") Integer userId, @RequestBody User updatedUser) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            User updated = userService.updateUser(userId, updatedUser);
            genericResponse.setMessage("User successfully updated");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (IllegalArgumentException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

    @DeleteMapping("/{user_id}") // !!CHECK OK!!
    public ResponseEntity<Object> deleteUser(@PathVariable("user_id") Integer userId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            userService.deleteUser(userId);
            genericResponse.setMessage("User deleted successfully.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    @GetMapping("/{user_id}/places") // !!CHECK OK!!
    public ResponseEntity<?> getPlacesByUserId(@PathVariable("user_id") Integer userId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            List<PlaceProjection> places = userService.getPlacesByUserId(userId).orElse(Collections.emptyList());
            return new ResponseEntity<>(places, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    @GetMapping("/valid-User")
    public ResponseEntity<String> checkValidUserToken() {

        return ResponseEntity.ok("{\"token\":\"valid\"}");
    }

    private UserDTO mapUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUsername(user.getTheUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(user.getPhoneNumber());

        return userDTO;
    }
}
