package com.cameinw.cameinwbackend.place.controller;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.request.FacilityRequest;
import com.cameinw.cameinwbackend.place.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {
    private final FacilityService facilityService;

    @Autowired
    public FacilityController(FacilityService facilityService) {this.facilityService = facilityService;}

    @GetMapping() // ----- check ok ------
    public List<Facility> getAllFacilities() {
        return facilityService.getAllFacilities();
    }

    @GetMapping("/{place_id}") // ----- check ok ------
    public ResponseEntity<?> getFacilitiesByPlaceId(@PathVariable("place_id") Integer placeId) {
        try {
            Facility facility = facilityService.getFacilityByPlaceId(placeId);
            return new ResponseEntity<>(facility, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/{place_id}") // ----- check ok ------
    public ResponseEntity<String> createFacility(@PathVariable("place_id") Integer placeId, @RequestBody FacilityRequest facilityRequest) {
        try {
            Facility createdFacility = facilityService.createFacility(placeId, facilityRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Facility successfully created.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/{place_id}/{facility_id}") // ----- check ok ------
    public ResponseEntity<String> updateFacility(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("facility_id") Integer facilityId,
            @RequestBody FacilityRequest facilityRequest
    ) {
        try {
            Facility updatedFacility = facilityService.updateFacility(placeId, facilityId, facilityRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Facility successfully updated.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{place_id}/{facility_id}") // ----- check ok ------
    public ResponseEntity<String> deleteFacility(@PathVariable("place_id") Integer placeId,@PathVariable("facility_id") Integer facilityId) {
        try {
            facilityService.deleteFacility(placeId, facilityId);
            return ResponseEntity.ok("Facility successfully deleted.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
