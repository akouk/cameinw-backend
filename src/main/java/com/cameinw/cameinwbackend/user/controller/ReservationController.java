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
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReservationController {
    private final ReservationService reservationService;


    @PostMapping("/places/{place_id}/reservations") // !!! CHECK OK!!
    public ResponseEntity<String> makeReservation(
            @PathVariable("place_id") Integer placeId,
            @Valid @RequestBody ReservationRequest reservationRequest) {
        try {
            Reservation createReservation = reservationService.makeReservation(placeId, reservationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reservation successfully created.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/users/{user_id}/reservations") // !!! CHECK OK!!
    public ResponseEntity<?> getReservationsByUserId(@PathVariable("user_id") Integer userId) {
        try {
            List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(reservations);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/users/{user_id}/reservations/{reservation_id}") // !!! CHECK OK!!
    public ResponseEntity<?> getReservationByUserId(
            @PathVariable("user_id") Integer userId,
            @PathVariable("reservation_id") Integer reservationId) {
        try {
            Reservation reservation = reservationService.getReservationByUserId(userId, reservationId);
            return ResponseEntity.status(HttpStatus.OK).body(reservation);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/places/{place_id}/reservations") // !!! CHECK OK!!
    public ResponseEntity<?> getReservationsByPlaceId(@PathVariable("place_id") Integer placeId) {
        try {
            List<Reservation> reservations = reservationService.getReservationsByPlaceId(placeId);
            return ResponseEntity.status(HttpStatus.OK).body(reservations);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/places/{place_id}/reservations/{reservation_id}") // !!! CHECK OK!!
    public ResponseEntity<?> getReservationByPlaceId(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("reservation_id") Integer reservationId) {
        try {
            Reservation reservation = reservationService.getReservationByPlaceId(placeId, reservationId);
            return ResponseEntity.status(HttpStatus.OK).body(reservation);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
