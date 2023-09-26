package com.cameinw.cameinwbackend.place.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.request.AvailabilityRequest;
import com.cameinw.cameinwbackend.place.request.PlaceRequest;
import com.cameinw.cameinwbackend.place.service.PlaceService;
import com.cameinw.cameinwbackend.response.GenericResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/places")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @GetMapping()  // !!!CHECK OK!!!
    public ResponseEntity<?> getAllPlaces() {
        try {
            List<Place> places = placeService.getAllPlaces();
            return new ResponseEntity<>(places, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping() // !!!CHECK OK!!!
    public ResponseEntity<?> createPlace(@Valid @RequestBody PlaceRequest placeRequest) {

        // TODO: CHECK HOW TO PRINT WHICH PROPERTY FAILED THE VALIDATION
        try {
            Place createdPlace = placeService.createPlace(placeRequest);
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage("Place successfully created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/{place_id}") // !!!CHECK OK!!!
    public ResponseEntity<?> getPlaceById (@PathVariable("place_id") Integer placeId) {

        try {
            return new ResponseEntity<>(placeService.getPlaceByPlaceId(placeId), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

    }

    @PutMapping("/{place_id}") // !!!CHECK OK!!!
    public ResponseEntity<Object> updatePlace(@PathVariable("place_id") Integer placeId, @RequestBody PlaceRequest placeRequest) {
        try {
            Place updated = placeService.updatePlace(placeId, placeRequest);
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage("Place successfully updated");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{place_id}") // !!!CHECK OK!!!
    public ResponseEntity<Object> deletePlace(@PathVariable("place_id") Integer placeId) {
        try {
            placeService.deletePlace(placeId);
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage("Place deleted successfully.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{place_id}/owner") // !!!CHECK OK!!!
    public ResponseEntity<?> getOwner(@PathVariable("place_id") Integer placeId) {
        try {
            return new ResponseEntity<>(placeService.getOwner(placeId), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

//    @GetMapping("/availability")
//    public ResponseEntity<List<Place>> getAvailablePlaces(
//            @Valid @RequestBody AvailabilityRequest availabilityRequest) {
//
//        List<Place> availablePlaces = placeService.getAvailablePlaces(availabilityRequest);
//        if (availablePlaces.size()>0) {
//            return ResponseEntity.ok(availablePlaces);
//        } else {
//            return ResponseEntity.noContent().build();
//        }
//    }

    @GetMapping("/availability/{city}/{country}/{guests}/{checkIn}/{checkOut}")
    public ResponseEntity<List<Place>> getAvailablePlaces(
            @PathVariable("city") String city,
            @PathVariable("country") String country,
            @PathVariable("guests") Integer guests,
            @PathVariable("checkIn") @DateTimeFormat(pattern =
                    "yyyy-MM-dd") Date checkIn,
            @PathVariable("checkOut") @DateTimeFormat(pattern =
                    "yyyy-MM-dd") Date checkOut
    ) {

        List<Place> availablePlaces =
                placeService.getAvailablePlaces(city, country, guests, checkIn,
                        checkOut);
        if (availablePlaces.size()>0) {
            return ResponseEntity.ok(availablePlaces);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
