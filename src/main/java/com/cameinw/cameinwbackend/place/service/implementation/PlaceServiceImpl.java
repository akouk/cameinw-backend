package com.cameinw.cameinwbackend.place.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.request.PlaceRequest;
import com.cameinw.cameinwbackend.place.service.PlaceService;
import com.cameinw.cameinwbackend.user.enums.Role;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceServiceImpl(UserRepository userRepository, PlaceRepository placeRepository) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Place> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        checkIfPlacesWereFound(places);
        return places;
    }

    @Override
    public Place getPlaceByPlaceId(Integer placeId) {
        return getPlaceById(placeId);
    }

    @Override
    @Transactional
    public Place createPlace(PlaceRequest placeRequest) {
        User user = getUserById(placeRequest.getUser().getId());
        checkRole(user);

        Place newPlace = createNewPlace(placeRequest, user);
        return savePlace(newPlace);
    }

    @Override
    @Transactional
    public Place updatePlace(Integer placeId, PlaceRequest placeRequest) {
        User user = getUserById(placeRequest.getUser().getId());
        Place place = getPlaceById(placeId);
        checkUserOwnership(place, user.getId());

        copyOnlyNonNullProperties(place, placeRequest);
        return savePlace(place);
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

    private void checkIfPlacesWereFound(List<Place> places) {
        if (places.isEmpty()) {
            throw new ResourceNotFoundException("No places were found.");
        }
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    private Place getPlaceById(Integer placeId) {
        System.out.println("get place by placeId: " + placeId);
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
    }

    private void checkRole(User user) {
        if (user.getRole() != Role.OWNER) {
            throw new CustomUserFriendlyException("User is not an owner.");
        }
    }

    private void checkUserOwnership(Place place, Integer userId) {
        if (!isUserOwnerOfPlace(place, userId)) {
            throw new CustomUserFriendlyException("User is not the owner of the place.");
        }
    }

    private boolean isUserOwnerOfPlace(Place place, Integer userId) {
        User placeOwner = place.getUser();
        return placeOwner != null && placeOwner.getId().equals(userId);
    }

    private Place createNewPlace(PlaceRequest placeRequest, User user) {
        placeRequest.validate();
        return Place.builder()
                .name(placeRequest.getName())
                .propertyType(placeRequest.getPropertyType())
                .description(placeRequest.getDescription())
                .cost(placeRequest.getCost())
                .country(placeRequest.getCountry())
                .city(placeRequest.getCity())
                .address(placeRequest.getAddress())
                .latitude(placeRequest.getLatitude())
                .longitude(placeRequest.getLongitude())
                .area(placeRequest.getArea())
                .guests(placeRequest.getGuests())
                .bedrooms(placeRequest.getBedrooms())
                .beds(placeRequest.getBeds())
                .bathrooms(placeRequest.getBathrooms())
                .user(user)
                .build();
    }

    private void copyOnlyNonNullProperties(Place place, PlaceRequest placeRequest) {
        if (placeRequest.getName() != null) {
            place.setName(placeRequest.getName());
        }
        if (placeRequest.getPropertyType() != null) {
            place.setPropertyType(placeRequest.getPropertyType());
        }
        if (placeRequest.getDescription() != null) {
            place.setDescription(placeRequest.getDescription());
        }
        if (placeRequest.getCost() != null) {
            place.setCost(placeRequest.getCost());
        }
        if (placeRequest.getCountry() != null) {
            place.setCountry(placeRequest.getCountry());
        }
        if (placeRequest.getCity() != null) {
            place.setCity(placeRequest.getCity());
        }
        if (placeRequest.getAddress() != null) {
            place.setAddress(placeRequest.getAddress());
        }
        if (placeRequest.getLatitude() != null) {
            place.setLatitude(placeRequest.getLatitude());
        }
        if (placeRequest.getLongitude() != null) {
            place.setLongitude(placeRequest.getLongitude());
        }
        if (placeRequest.getArea() != null) {
            place.setArea(placeRequest.getArea());
        }
        if (placeRequest.getGuests() != null) {
            place.setGuests(placeRequest.getGuests());
        }
        if (placeRequest.getBedrooms() != null) {
            place.setBedrooms(placeRequest.getBedrooms());
        }
        if (placeRequest.getBeds() != null) {
            place.setBeds(placeRequest.getBeds());
        }
        if (placeRequest.getBathrooms() != null) {
            place.setBathrooms(placeRequest.getBathrooms());
        }
    }

    private Place savePlace(Place place) {return  placeRepository.save(place);}
}
