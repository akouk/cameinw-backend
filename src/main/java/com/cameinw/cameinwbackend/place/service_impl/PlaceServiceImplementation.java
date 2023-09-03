package com.cameinw.cameinwbackend.place.service_impl;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.request.PlaceRequest;
import com.cameinw.cameinwbackend.place.service.PlaceService;
import com.cameinw.cameinwbackend.user.enums.Role;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceServiceImplementation implements PlaceService {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceServiceImplementation(UserRepository userRepository, PlaceRepository placeRepository) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    @Override
    public Optional<Place> getPlaceByPlaceId(Integer placeId) {
        return placeRepository.findById(placeId);
    }

    @Override
    @Transactional
    public Place createPlace(PlaceRequest placeRequest) {
        placeRequest.validate();

        User user = getUserById(placeRequest.getUserId());

        if (user.getRole() != Role.OWNER) {
            throw new CustomUserFriendlyException("User is not an owner.");
        }

        Place newPlace = new Place();
        BeanUtils.copyProperties(placeRequest, newPlace);
        newPlace.setUser(user);

        return placeRepository.save(newPlace);
    }

    @Override
    @Transactional
    public Place updatePlace(Integer placeId, PlaceRequest placeRequest) {
        placeRequest.validate();

        Place existingPlace = getPlaceById(placeId);

        if (!isUserOwnerOfPlace(existingPlace, placeRequest.getUserId())) {
            throw new CustomUserFriendlyException("User is not the owner of the place.");
        }

        BeanUtils.copyProperties(placeRequest, existingPlace);

        return placeRepository.save(existingPlace);
    }

    @Override
    @Transactional
    public void deletePlace(Integer placeId) {
        Place place = getPlaceById(placeId);
        placeRepository.delete(place);
    }

    @Override
    public User getOwner(Integer placeId) {
        Place place = getPlaceById(placeId);
        return place.getUser();
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }
    private Place getPlaceById(Integer placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
    }

    private boolean isUserOwnerOfPlace(Place place, Integer userId) {
        User placeOwner = place.getUser();
        return placeOwner != null && placeOwner.getId().equals(userId);
    }
}
