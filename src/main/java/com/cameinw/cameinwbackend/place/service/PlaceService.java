package com.cameinw.cameinwbackend.place.service;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.request.PlaceRequest;
import com.cameinw.cameinwbackend.user.model.User;

import java.util.List;
import java.util.Optional;

public interface PlaceService {
    List<Place> getAllPlaces();
    Optional<Place> getPlaceById(Integer placeId);
    Place createPlace(PlaceRequest placeRequest);
    Place updatePlace(Integer placeId, Place updatedPlace);
    void deletePlace(Integer placeId);
    User getOwner(Integer placeId);
}
