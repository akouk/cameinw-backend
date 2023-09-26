package com.cameinw.cameinwbackend.place.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceAlreadyExistException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.request.FacilityRequest;
import com.cameinw.cameinwbackend.place.service.FacilityService;
import com.cameinw.cameinwbackend.response.GenericResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FacilityController {
    private final FacilityService facilityService;

    @Autowired
    public FacilityController(FacilityService facilityService) {this.facilityService = facilityService;}

    @GetMapping("/facilities") // ----- check ok ------
    public ResponseEntity<?> getAllFacilities() {
        try {
            List<Facility> facilities = facilityService.getAllFacilities();
            return new ResponseEntity<>(facilities, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/places/{place_id}/facilities") // !!! CHECK OK !!!
    public ResponseEntity<?> getFacilitiesByPlaceId(@PathVariable("place_id") Integer placeId) {
        try {
            Facility facility = facilityService.getFacilityByPlaceId(placeId);
            return new ResponseEntity<>(facility, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/places/{place_id}/facilities") // !!! CHECK OK !!!
    public ResponseEntity<Object> createFacility(@PathVariable("place_id") Integer placeId, @Valid @RequestBody FacilityRequest facilityRequest) {
        try {
            Facility createdFacility = facilityService.createFacility(placeId, facilityRequest);
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage("Facility successfully created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (ResourceAlreadyExistException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/places/{place_id}/facilities/{facility_id}") // !!! CHECK OK !!!
    public ResponseEntity<Object> updateFacility(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("facility_id") Integer facilityId,
            @Valid  @RequestBody FacilityRequest facilityRequest
    ) {
        try {
            Facility updatedFacility = facilityService.updateFacility(placeId, facilityId, facilityRequest);
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage("Facility successfully updated.");
            return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/places/{place_id}/facilities/{facility_id}") // !!! CHECK OK !!!
    public ResponseEntity<Object> deleteFacility(@PathVariable("place_id") Integer placeId, @PathVariable("facility_id") Integer facilityId) {
        try {
            facilityService.deleteFacility(placeId, facilityId);
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage("Facility successfully deleted.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
