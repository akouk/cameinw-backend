package com.cameinw.cameinwbackend.place.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceAlreadyExistException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.FacilityRepository;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.request.FacilityRequest;
import com.cameinw.cameinwbackend.place.service.FacilityService;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilityServiceImpl implements FacilityService {
    private final FacilityRepository facilityRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    @Autowired
    public FacilityServiceImpl(FacilityRepository facilityRepository, PlaceRepository placeRepository, UserRepository userRepository) {
        this.facilityRepository = facilityRepository;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Facility> getAllFacilities() {
        List<Facility> facilities = facilityRepository.findAll();
        checkIfFacilitiesWereFound(facilities);
        return facilities;
    }

    @Override
    public Facility getFacilityByPlaceId(Integer placeId) {
        Facility facility = facilityRepository.findByPlaceId(placeId);
        checkIfFacilitiesWereFoundForThePlace(facility);
        return facility;
    }

    @Override
    @Transactional
    public Facility createFacility(Integer placeId, FacilityRequest facilityRequest) {
        Place place = getPlaceById(placeId);
        User user = getUserById(facilityRequest.getUser().getId());
        checkUserOwnership(place, user.getId());

        checkIfFacilitiesExist(placeId);
        Facility facility = createNewFacility(facilityRequest, place);
        return saveFacility(facility);
    }

    @Override
    @Transactional
    public Facility updateFacility(Integer placeId, Integer facilityId, FacilityRequest facilityRequest) {
        User user = getUserById(facilityRequest.getUser().getId());
        Place place = getPlaceById(placeId);
        checkUserOwnership(place, user.getId());

        Facility facility = getFacilityById(facilityId);
        copyOnlyNonNullProperties(facilityRequest, facility);
        return saveFacility(facility);
    }

    @Override
    @Transactional
    public void deleteFacility(Integer placeId, Integer facilityId) {
        Place place = getPlaceById(placeId);
        Facility facility = getFacilityById(facilityId);
        facilityRepository.delete(facility);
    }

    private void checkIfFacilitiesWereFound(List<Facility> facilities) {
        if (facilities.isEmpty()) {
            throw new ResourceNotFoundException("No facilities were found.");
        }
    }

    private void checkIfFacilitiesWereFoundForThePlace(Facility facility) {
        if (facility == null) {
            throw new ResourceNotFoundException("Facility not found for the specified place.");
        }
    }

    private Facility getFacilityById(Integer facilityId) {
        return facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found."));
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    private Place getPlaceById(Integer placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
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

    @Override
    public boolean hasExistingFacility(Integer placeId) {
        return facilityRepository.existsByPlaceId(placeId);
    }

    private void checkIfFacilitiesExist(Integer placeId) {
        if (hasExistingFacility(placeId)) {
            throw new ResourceAlreadyExistException("A facility already exists for this place. You can only update it.");
        }
    }

    private Facility createNewFacility(FacilityRequest facilityRequest, Place place) {
        return Facility.builder()
                .hasFreeParking(facilityRequest.isHasFreeParking())
                .isNonSmoking(facilityRequest.isNonSmoking())
                .hasFreeWiFi(facilityRequest.isHasFreeWiFi())
                .hasbreakfast(facilityRequest.getHasbreakfast())
                .hasbalcony(facilityRequest.isHasbalcony())
                .hasSwimmingPool(facilityRequest.isHasSwimmingPool())
                .place(place)
                .build();
    }

    private void copyOnlyNonNullProperties(FacilityRequest facilityRequest, Facility facility) {
        if (facilityRequest.isHasFreeParking() != false) {
            facility.setHasFreeParking(facilityRequest.isHasFreeParking());
        }
        if (facilityRequest.isNonSmoking() != false) {
            facility.setNonSmoking(facilityRequest.isNonSmoking());
        }
        if (facilityRequest.isHasFreeWiFi() != false) {
            facility.setHasFreeWiFi(facilityRequest.isHasFreeWiFi());
        }
        if (facilityRequest.getHasbreakfast() != null) {
            facility.setHasbreakfast(facilityRequest.getHasbreakfast());
        }
        if (facilityRequest.isHasbalcony() != false) {
            facility.setHasbalcony(facilityRequest.isHasbalcony());
        }
        if (facilityRequest.isHasSwimmingPool() != false) {
            facility.setHasSwimmingPool(facilityRequest.isHasSwimmingPool());
        }
    }


    private Facility saveFacility(Facility facility) {
        return facilityRepository.save(facility);
    }


}
