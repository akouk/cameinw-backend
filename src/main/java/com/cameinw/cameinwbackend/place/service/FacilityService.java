package com.cameinw.cameinwbackend.place.service;

import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.request.FacilityRequest;

import java.util.List;

public interface FacilityService {
    List<Facility> getAllFacilities();
    Facility getFacilityByPlaceId(Integer placeId);
    Facility createFacility(Integer placeId, FacilityRequest facilityRequest);
    Facility updateFacility(Integer placeId, Integer facilityId, FacilityRequest facilityRequest);
    void deleteFacility(Integer placeId, Integer facilityId);
    boolean hasExistingFacility(Integer placeId);
}
