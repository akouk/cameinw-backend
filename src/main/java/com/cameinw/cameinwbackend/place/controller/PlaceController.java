package com.cameinw.cameinwbackend.place.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.request.PlaceRequest;
import com.cameinw.cameinwbackend.place.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            return ResponseEntity.status(HttpStatus.CREATED).body("Place successfully created.");
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
    public ResponseEntity<?> updatePlace(@PathVariable("place_id") Integer placeId, @RequestBody PlaceRequest placeRequest) {
        try {
            System.out.println("Place id in controller: " + placeId);
            Place updated = placeService.updatePlace(placeId, placeRequest);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Place successfully updated");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{place_id}") // !!!CHECK OK!!!
    public ResponseEntity<?> deletePlace(@PathVariable("place_id") Integer placeId) {
        try {
            placeService.deletePlace(placeId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Place deleted successfully.");
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
}
