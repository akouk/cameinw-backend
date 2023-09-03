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
        try {
            System.out.println("Attempting to retrieve all places.");
            List<Place> places = placeRepository.findAll();
            System.out.println("Retrieved" + places.size() + " places from the database.");
            return places;
        } catch (Exception ex) {
            System.out.println("An error occurred while retrieving all places: " + ex.getMessage());
            throw ex; // Re-throw the exception to be handled at the controller level.
        }
//        return
//                placeRepository.findAll();
    }

    @Override
    public Optional<Place> getPlaceById(Integer placeId) {
        return placeRepository.findById(placeId);
    }

    @Override
    @Transactional
    public Place createPlace(PlaceRequest placeRequest) {
        User user = userRepository.findById(placeRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (user.getRole() != Role.OWNER) {
            throw new CustomUserFriendlyException("User is not an owner.");
        }

        Place newPlace = Place.builder()
                .name(placeRequest.getName())
                .propertyType(placeRequest.getPropertyType())
                .description(placeRequest.getDescription())
                .cost(placeRequest.getCost())
                .country(placeRequest.getCountry())
                .city(placeRequest.getCity())
                .address(placeRequest.getAddress())
                .longitude(placeRequest.getLongitude())
                .latitude(placeRequest.getLatitude())
                .area(placeRequest.getArea())
                .guests(placeRequest.getGuests())
                .bedrooms(placeRequest.getBedrooms())
                .beds(placeRequest.getBeds())
                .bathrooms(placeRequest.getBathrooms())
                .user(user)
                .build();

        return placeRepository.save(newPlace);
    }

    @Override
    @Transactional
    public Place updatePlace(Integer placeId, Place updatedPlace) {
        return placeRepository.findById(placeId)
                .map(existingPlace -> {
                    existingPlace.setName(updatedPlace.getName());
                    existingPlace.setPropertyType(updatedPlace.getPropertyType());
                    existingPlace.setCountry(updatedPlace.getCountry());
                    existingPlace.setCity(updatedPlace.getCity());
                    existingPlace.setAddress(updatedPlace.getAddress());
                    existingPlace.setCost(updatedPlace.getCost());
                    existingPlace.setDescription(updatedPlace.getDescription());
                    existingPlace.setGuests(updatedPlace.getGuests());
                    existingPlace.setBathrooms(updatedPlace.getBathrooms());
                    existingPlace.setBedrooms(updatedPlace.getBedrooms());
                    existingPlace.setBeds(updatedPlace.getBeds());
                    existingPlace.setMainImage(updatedPlace.getMainImage());
                    existingPlace.setArea(updatedPlace.getArea());
                    return placeRepository.save(existingPlace);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
    }

    @Override
    @Transactional
    public void deletePlace(Integer placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
        placeRepository.delete(place);
    }

    @Override
    public User getOwner(Integer placeId) {
        return placeRepository.findById(placeId)
                .map(place -> place.getUser())
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
    }
}
