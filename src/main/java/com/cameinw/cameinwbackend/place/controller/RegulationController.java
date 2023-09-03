package com.cameinw.cameinwbackend.place.controller;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.request.RegulationRequest;
import com.cameinw.cameinwbackend.place.service.RegulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/regulations")
public class RegulationController {
    private final RegulationService regulationService;

    @Autowired
    public RegulationController(RegulationService regulationService) {
        this.regulationService = regulationService;
    }

    @GetMapping()
    public List<Regulation> getAllRegulations() {
        return regulationService.getAllRegulations();
    }

    @GetMapping("/{place_id}") // ----- check ok ------
    public ResponseEntity<?> getRegulationByPlaceId(@PathVariable("place_id") Integer placeId) {
        try {
            Regulation regulation = regulationService.getRegulationByPlaceId(placeId);
            return new ResponseEntity<>(regulation, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/{place_id}") // ----- check ok ------
    public ResponseEntity<String> createRegulation(@PathVariable("place_id") Integer placeId, @RequestBody RegulationRequest regulationRequest) {
        try {
            Regulation createdRegulation = regulationService.createRegulation(placeId, regulationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Regulation successfully created.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/{place_id}/{regulation_id}") // ----- check ok ------
    public ResponseEntity<String> updateRegulation(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("regulation_id") Integer regulationId,
            @RequestBody RegulationRequest regulationRequest
    ) {
        try {
            Regulation updatedRegulation = regulationService.updateRegulation(placeId, regulationId, regulationRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Regulation successfully updated.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{place_id}/{regulation_id}") // ----- check ok ------
    public ResponseEntity<String> deleteRegulation(@PathVariable("place_id") Integer placeId,@PathVariable("regulation_id") Integer regulationId) {
        try {
            regulationService.deleteRegulation(placeId, regulationId);
            return ResponseEntity.ok("Regulation successfully deleted.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
