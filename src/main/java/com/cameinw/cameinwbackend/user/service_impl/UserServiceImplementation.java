package com.cameinw.cameinwbackend.user.service_impl;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.projection.PlaceProjection;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, PlaceRepository placeRepository) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    public User updateUser(Integer userId, User updatedUser) {
        Optional<User> optionalExistingUser = userRepository.findById(userId);
        if (optionalExistingUser.isPresent()) {
            User existingUser = optionalExistingUser.get();
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            return userRepository.save(existingUser);
        } else {
            throw new ResourceNotFoundException("User not found.");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new ResourceNotFoundException("User not found.");
        }
    }

    @Override
    public Optional<List<PlaceProjection>> getPlacesByUserId(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return placeRepository.findPlacesByUserId(userId);
        } else {
            throw new ResourceNotFoundException("User not found.");
        }
    }
}
