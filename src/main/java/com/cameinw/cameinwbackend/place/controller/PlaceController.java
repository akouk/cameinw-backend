package com.cameinw.cameinwbackend.place.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.request.PlaceRequest;
import com.cameinw.cameinwbackend.place.service.PlaceService;
import com.cameinw.cameinwbackend.user.model.User;
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

    @GetMapping()  //---- check ok -----
    public List<Place> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/{place_id}") //---- check ok -----
    public ResponseEntity<?> getPlaceById (@PathVariable("place_id") Integer placeId) {

        try {
            return new ResponseEntity<>(placeService.getPlaceByPlaceId(placeId), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

    }

    @PostMapping() //---- check ok -----
    public ResponseEntity<String> createPlace(@Valid @RequestBody PlaceRequest placeRequest) {

        // TODO: CHECK HOW TO PRINT WHICH PROPERTY FAILED THE VALIDATION
        try {
            Place createdPlace = placeService.createPlace(placeRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Place successfully created."); // STATUS: 201
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); // STATUS: 400
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); //STATUS: 404
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); // STATUS: 400
        }
    }

    @PutMapping("/{place_id}") //---- check ok -----
    public ResponseEntity<String> updatePlace(@PathVariable("place_id") Integer placeId,
                                              @RequestBody PlaceRequest placeRequest) {
        try {
            Place updated = placeService.updatePlace(placeId, placeRequest);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Place successfully updated"); // STATUS: 200 OK
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); // STATUS: 404 Not Found
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); // STATUS: 400
        }
    }

    @DeleteMapping("/{place_id}") //---- check ok -----
    public ResponseEntity<String> deletePlace(@PathVariable("place_id") Integer placeId) {
        try {
            placeService.deletePlace(placeId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Place deleted successfully."); // STATUS: 204 No Content
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); // STATUS: 404 Not Found
        }
    }

    @GetMapping("/{place_id}/owner") //---- check ok -----
    public ResponseEntity<?> getOwner(@PathVariable("place_id") Integer placeId) {
        try {
            return new ResponseEntity<>(placeService.getOwner(placeId), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
