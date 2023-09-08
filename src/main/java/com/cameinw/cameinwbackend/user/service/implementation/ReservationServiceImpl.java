package com.cameinw.cameinwbackend.user.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Facility;
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

    public Reservation makeReservation(Integer placeId, ReservationRequest reservationRequest) {
        Place place = getPlaceById(placeId);
        User user = getUserById(reservationRequest.getUser().getId());
        checkUserOwnership(place, user.getId());

        checkReservationValidation(reservationRequest, place);
        Reservation reservation = createReservation(reservationRequest, place, user);
        return saveReservation(reservation);
    }

    @Override
    public List<Reservation> getReservationsByUserId (Integer userId){
        User user = getUserById(userId);
        List<Reservation> reservations = reservationRepository.findByUser(user);
        checkIfReservationsExistForResource(reservations);
        return reservations;
    }

    @Override
    public Reservation getReservationByUserId (Integer userId, Integer reservationId){
        User user = getUserById(userId);
        Reservation reservation = getReservationById(reservationId);
        return reservation;
    }

    @Override
    public List<Reservation> getReservationsByPlaceId (Integer placeId){
        Place place = getPlaceById(placeId);
        List<Reservation> reservations = reservationRepository.findByPlace(place);
        checkIfReservationsExistForResource(reservations);
        return reservations;
    }

    @Override
    public Reservation getReservationByPlaceId (Integer placeId, Integer reservationId){
        Place place = getPlaceById(placeId);
        Reservation reservation = getReservationById(reservationId);
        return reservation;
    }

    private Place getPlaceById(Integer placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    private Reservation getReservationById(Integer reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found."));
    }

    public void checkIfReservationsExistForResource(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            throw new ResourceNotFoundException("No reservations found.");
        }
    }


    private boolean isUserOwnerOfPlace(Place place, Integer userId) {
        User placeOwner = place.getUser();
        return placeOwner != null && placeOwner.getId().equals(userId);
    }

    private void checkUserOwnership(Place place, Integer userId) {
        if (!isUserOwnerOfPlace(place, userId)) {
            throw new CustomUserFriendlyException("User is not the owner of the place.");
        }
    }

    private boolean isValidReservation(ReservationRequest reservationRequest, Place place) {
        List<Reservation> existingReservations = reservationRepository.findBetweenDates(
                reservationRequest.getCheckIn(),
                reservationRequest.getCheckOut(),
                place);

        return existingReservations.isEmpty()
                && reservationRequest.getCheckIn().compareTo(reservationRequest.getCheckOut()) < 0;
    }

    private void checkReservationValidation(ReservationRequest reservationRequest, Place place) {
        if (!isValidReservation(reservationRequest, place)) {
            throw new CustomUserFriendlyException("Failed to create reservation.");
        }
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
