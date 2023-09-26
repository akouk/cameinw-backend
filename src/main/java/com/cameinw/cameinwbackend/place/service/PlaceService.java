package com.cameinw.cameinwbackend.place.service;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.request.AvailabilityRequest;
import com.cameinw.cameinwbackend.place.request.PlaceRequest;
import com.cameinw.cameinwbackend.user.model.User;

import java.util.Date;
import java.util.List;

public interface PlaceService {
    List<Place> getAllPlaces();
    Place getPlaceByPlaceId(Integer placeId);
    Place createPlace(PlaceRequest placeRequest);
    Place updatePlace(Integer placeId, PlaceRequest placeRequest);
    void deletePlace(Integer placeId);
    User getOwner(Integer placeId);
//    List<Place> getAvailablePlaces(AvailabilityRequest availabilityRequest);
    List<Place> getAvailablePlaces(String city, String country, Integer guests, Date checkIn, Date checkOut);

}
