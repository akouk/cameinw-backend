package com.cameinw.cameinwbackend.place.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceAlreadyExistException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.request.RegulationRequest;
import com.cameinw.cameinwbackend.place.service.RegulationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RegulationController {
    private final RegulationService regulationService;

    @Autowired
    public RegulationController(RegulationService regulationService) {
        this.regulationService = regulationService;
    }

    @GetMapping("/regulations") // !!!CHECK OK!!!
    public ResponseEntity<?> getAllRegulations() {

        try {
            List<Regulation> regulations = regulationService.getAllRegulations();
            return new ResponseEntity<>(regulations, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/places/{place_id}/regulations") // !!!CHECK OK!!!
    public ResponseEntity<?> getRegulationByPlaceId(@PathVariable("place_id") Integer placeId) {
        try {
            Regulation regulation = regulationService.getRegulationByPlaceId(placeId);
            return new ResponseEntity<>(regulation, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/places/{place_id}/regulations") // !!!CHECK OK!!!
    public ResponseEntity<String> createRegulation(
            @PathVariable("place_id") Integer placeId,
            @Valid @RequestBody RegulationRequest regulationRequest) {
        try {
            Regulation createdRegulation = regulationService.createRegulation(placeId, regulationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Regulation successfully created.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (ResourceAlreadyExistException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/places/{place_id}/regulations/{regulation_id}") // !!!CHECK OK!!!
    public ResponseEntity<String> updateRegulation(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("regulation_id") Integer regulationId,
            @Valid  @RequestBody RegulationRequest regulationRequest
    ) {
        try {
            Regulation updatedRegulation = regulationService.updateRegulation(placeId, regulationId, regulationRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Regulation successfully updated.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/places/{place_id}/regulations/{regulation_id}") // !!!CHECK OK!!!
    public ResponseEntity<String> deleteRegulation(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("regulation_id") Integer regulationId) {
        try {
            regulationService.deleteRegulation(placeId, regulationId);
            return ResponseEntity.ok("Regulation successfully deleted.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
