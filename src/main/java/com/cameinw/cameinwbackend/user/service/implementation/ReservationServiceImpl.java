package com.cameinw.cameinwbackend.user.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.ReservationRepository;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.user.request.ReservationRequest;
import com.cameinw.cameinwbackend.user.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    private final PlaceRepository placeRepository;


    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  UserRepository userRepository,
                                  PlaceRepository placeRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;

    }

    @Override
    public Reservation getReservationById(Integer reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found."));
    }

    @Override
    public User getUserByReservationId(Integer reservationId) {
        Reservation reservation = getReservationById(reservationId);
        return reservation.getUser();
    }

    @Override
    public Place getPlaceByReservationId(Integer reservationId) {
        Reservation reservation = getReservationById(reservationId);
        return reservation.getPlace();
    }

    public Reservation makeReservation(ReservationRequest reservationRequest) {
        Place place = reservationRequest.getPlace();
        getPlaceById(place.getId());
        User user = reservationRequest.getUser();
        getUserById(user.getId());

        // Check if the user owns the place
        if (isUserOwnerOfPlace(place, user.getId())) {
            throw new CustomUserFriendlyException("You cannot make a reservation for your own place.");
        }

        if (isValidReservation(reservationRequest, place)) {
            Reservation newReservation = createReservation(reservationRequest, place, user);
            return saveReservation(newReservation);
        } else {
            throw new CustomUserFriendlyException("Failed to create reservation.");
        }
    }

    private Place getPlaceById(Integer placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    private boolean isValidReservation(ReservationRequest reservationRequest, Place place) {
        List<Reservation> existingReservations = reservationRepository.findBetweenDates(
                reservationRequest.getCheckIn(),
                reservationRequest.getCheckOut(),
                place);

        return existingReservations.isEmpty()
                && reservationRequest.getCheckIn().compareTo(reservationRequest.getCheckOut()) < 0;
    }

    private boolean isUserOwnerOfPlace(Place place, Integer userId) {
        User placeOwner = place.getUser();
        return placeOwner != null && placeOwner.getId().equals(userId);
    }

    private Reservation createReservation(ReservationRequest reservationRequest, Place place, User user) {
        return Reservation.builder()
                .checkIn(reservationRequest.getCheckIn())
                .checkOut(reservationRequest.getCheckOut())
                .user(user)
                .place(place)
                .build();
    }

    private Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}
