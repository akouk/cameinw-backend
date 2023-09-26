package com.cameinw.cameinwbackend.user.service.implementation;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.projection.PlaceProjection;
import com.cameinw.cameinwbackend.user.dto.UserDTO;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            PlaceRepository placeRepository
    ) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users were found.");
        }
        return users;
    }

    @Override
    public User getUserByUserId(Integer userId) {
        return getUserById(userId);
    }

    @Override
    @Transactional
    public User updateUser(Integer userId, User updatedUser) {
        User existingUser = getUserById(userId);

        if (updatedUser.getFirstName()!=null){existingUser.setFirstName(updatedUser.getFirstName());}
        if (updatedUser.getLastName()!=null){existingUser.setLastName(updatedUser.getLastName());}
        if (updatedUser.getPhoneNumber()!=null){existingUser.setPhoneNumber(updatedUser.getPhoneNumber());}
        if (updatedUser.getImageName()!=null){existingUser.setImageName(updatedUser.getImageName());}

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

    private UserDTO mapUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(userDTO.getPhone());

        return userDTO;
    }

}
