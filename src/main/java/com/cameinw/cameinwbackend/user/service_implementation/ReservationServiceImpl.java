package com.cameinw.cameinwbackend.user.service_implementation;

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
    public List<Reservation> getReservationsByUserId (Integer userId){
        User user = getUserById(userId);
        return reservationRepository.findByUser(user);
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

//    public Reservation makeReservation(Integer placeId, ReservationRequest reservationRequest) {
//
//        Place place = placeRepository.findById(placeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
//
//        User user = userRepository.findById(reservationRequest.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
//
//
//
//        List<Reservation> reservationExist = reservationRepository
//                .findBetweenDates(reservationRequest.getCheckIn(),
//                        reservationRequest.getCheckOut(),
//                        place);
//
//
//        /* Checks if the user and the place exist, then if
//         * the check in date is smaller than the check out date
//         * and finally if the dates are available
//         * IF SO, A RESERVATION IS MADE*/
//
//        if (user != null
//                && place != null
//                && reservationExist.size() == 0
//                && reservationRequest.getCheckIn().compareTo(reservationRequest.getCheckOut()) < 0) {
//            var newReservation = Reservation.builder()
//                    .checkIn(reservationRequest.getCheckIn())
//                    .checkOut(reservationRequest.getCheckOut())
//                    .user(user)
//                    .place(place)
//                    .build();
//
//            reservationRepository.save(newReservation);
//            return newReservation;
//        }
//        return null;
//    }

    public Reservation makeReservation(Integer placeId, ReservationRequest reservationRequest) {
        Place place = getPlaceById(placeId);
        User user = getUserById(reservationRequest.getUserId());

        if (place != null && user != null && isValidReservation(reservationRequest, place)) {
            Reservation newReservation = createReservation(reservationRequest, place, user);
            return saveReservation(newReservation);
        }

        return null;
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
