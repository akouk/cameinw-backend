package com.cameinw.cameinwbackend.place.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.request.PlaceRequest;
import com.cameinw.cameinwbackend.place.service.PlaceService;
import com.cameinw.cameinwbackend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    private final PlaceRepository placeRepository;

    @GetMapping("/")  //---- check ok -----
    public List<Place> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/{place_id}") //---- check ok -----
    public ResponseEntity<Place> getPlaceById (@PathVariable("place_id") Integer placeId) {

        Optional<Place> place = placeService.getPlaceById(placeId);
        return place
                .map(ResponseEntity::ok) // STATUS: 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // STATUS: 404 Not Found
    }

    @PostMapping("/") //---- check ok -----
    public ResponseEntity<String> createPlace(@RequestBody PlaceRequest placeRequest) {
        try {
            Place createdPlace = placeService.createPlace(placeRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Place successfully created."); // STATUS: 201
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not an owner."); // STATUS: 400
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found."); //STATUS: 404
        }
    }

    @PutMapping("/{place_id}") //---- check ok -----
    public ResponseEntity<String> updatePlace(@PathVariable("place_id") Integer placeId,
                                              @RequestBody Place updatedPlace) {
        try {
            Place updated = placeService.updatePlace(placeId, updatedPlace);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Place with id " + placeId + " is updated"); // STATUS: 200 OK
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Place not found."); // STATUS: 404 Not Found
        }
    }

    @DeleteMapping("/{place_id}") //---- check ok -----
    public ResponseEntity<String> deletePlace(@PathVariable("place_id") Integer placeId) {
        try {
            placeService.deletePlace(placeId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Place deleted successfully."); // STATUS: 204 No Content

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Place not found."); // STATUS: 404 Not Found
        }
    }

    @GetMapping("/{place_id}/owner") //---- check ok -----
    public ResponseEntity<User> getOwner(@PathVariable("place_id") Integer placeId) {
        try {
            return ResponseEntity.ok(placeService.getOwner(placeId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
