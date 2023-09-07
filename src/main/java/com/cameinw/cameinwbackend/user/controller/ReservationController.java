package com.cameinw.cameinwbackend.user.controller;
import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.request.FacilityRequest;
import com.cameinw.cameinwbackend.place.request.RegulationRequest;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.request.ReservationRequest;
import com.cameinw.cameinwbackend.user.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;


    @PostMapping()
    public ResponseEntity<String> makeReservation(
            @Valid @RequestBody ReservationRequest reservationRequest) {
        try {
            Reservation createReservation = reservationService.makeReservation(reservationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reservation successfully created.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/{reservation_id}")
    public ResponseEntity<Object> getReservationById(@PathVariable("reservation_id") Integer reservationId) {
        try {
            Reservation reservation = reservationService.getReservationById(reservationId);
            return ResponseEntity.status(HttpStatus.OK).body(reservation);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{reservation_id}/user")
    public ResponseEntity<Object> getUserByReservationId(@PathVariable("reservation_id") Integer reservationId) {
        try {
            User user = reservationService.getUserByReservationId(reservationId);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{reservation_id}/place")
    public ResponseEntity<Object> getPlaceByReservationId(@PathVariable("reservation_id") Integer reservationId) {
        try {
            Place place = reservationService.getPlaceByReservationId(reservationId);
            return ResponseEntity.status(HttpStatus.OK).body(place);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

}
