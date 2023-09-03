package com.cameinw.cameinwbackend.place.service_impl;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.repository.FacilityRepository;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.repository.RegulationRepository;
import com.cameinw.cameinwbackend.place.request.FacilityRequest;
import com.cameinw.cameinwbackend.place.request.RegulationRequest;
import com.cameinw.cameinwbackend.place.service.FacilityService;
import com.cameinw.cameinwbackend.user.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilityServiceImplementation implements FacilityService {
    private final FacilityRepository facilityRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public FacilityServiceImplementation(FacilityRepository facilityRepository, PlaceRepository placeRepository) {
        this.facilityRepository = facilityRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    @Override
    public Facility getFacilityByPlaceId(Integer placeId) {
        return facilityRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found for the specified place."));
    }

    @Override
    @Transactional
    public Facility createFacility(Integer placeId, FacilityRequest facilityRequest) {
        Place place = getPlaceById(placeId);

        checkUserOwnership(place, facilityRequest.getUserId());

        if (hasExistingFacility(placeId)) {
            throw new CustomUserFriendlyException("A facility already exists for this place. You can only update it.");
        }

        Facility facility = new Facility();
        BeanUtils.copyProperties(facilityRequest, facility);
        facility.setPlace(place);

        return facilityRepository.save(facility);
    }

    @Override
    @Transactional
    public Facility updateFacility(Integer placeId, Integer facilityId, FacilityRequest facilityRequest) {
        Place place = getPlaceById(placeId);
        Facility existingFacility = getFacilityById(facilityId);

        checkUserOwnership(place, facilityRequest.getUserId());

        BeanUtils.copyProperties(facilityRequest, existingFacility);
        existingFacility.setPlace(place);

        return facilityRepository.save(existingFacility);
    }

    @Override
    @Transactional
    public void deleteFacility(Integer placeId, Integer facilityId) {
        getPlaceById(placeId);
        Facility facility = getFacilityById(facilityId);
        facilityRepository.delete(facility);
    }

    private Facility getFacilityById(Integer facilityId) {
        return facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found."));
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

}
