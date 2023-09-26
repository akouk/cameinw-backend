package com.cameinw.cameinwbackend.place.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceAlreadyExistException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.request.RegulationRequest;
import com.cameinw.cameinwbackend.place.service.RegulationService;
import com.cameinw.cameinwbackend.response.GenericResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Object> createRegulation(
            @PathVariable("place_id") Integer placeId,
            @Valid @RequestBody RegulationRequest regulationRequest) {
        try {
            Regulation createdRegulation = regulationService.createRegulation(placeId, regulationRequest);
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage("Regulation successfully created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (ResourceAlreadyExistException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/places/{place_id}/regulations/{regulation_id}") // !!!CHECK OK!!!
    public ResponseEntity<Object> updateRegulation(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("regulation_id") Integer regulationId,
            @Valid  @RequestBody RegulationRequest regulationRequest
    ) {
        try {
            Regulation updatedRegulation = regulationService.updateRegulation(placeId, regulationId, regulationRequest);
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage("Regulation successfully updated.");
            return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/places/{place_id}/regulations/{regulation_id}") // !!!CHECK OK!!!
    public ResponseEntity<Object> deleteRegulation(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("regulation_id") Integer regulationId) {
        try {
            regulationService.deleteRegulation(placeId, regulationId);
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setMessage("Regulation successfully deleted.");
            return ResponseEntity.ok(genericResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
