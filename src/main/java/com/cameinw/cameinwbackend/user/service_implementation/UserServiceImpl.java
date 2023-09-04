package com.cameinw.cameinwbackend.user.service_implementation;

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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PlaceRepository placeRepository) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByUserId(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    public User updateUser(Integer userId, User updatedUser) {
        User existingUser = getUserById(userId);

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    @Override
    public Optional<List<PlaceProjection>> getPlacesByUserId(Integer userId) {
        User user = getUserById(userId);
        return placeRepository.findPlacesByUserId(userId);
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

}
