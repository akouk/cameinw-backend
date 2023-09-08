package com.cameinw.cameinwbackend.user.service;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.request.ReservationRequest;

import java.util.List;

public interface ReservationService {
    Reservation makeReservation(Integer placeId, ReservationRequest reservationRequest);
    List<Reservation> getReservationsByUserId (Integer userId);
    Reservation getReservationByUserId(Integer userId, Integer reservationId);
    List<Reservation> getReservationsByPlaceId (Integer placeId);
    Reservation getReservationByPlaceId(Integer placeId, Integer reservationId);



//    User getUserByReservationId(Integer reservationId);
//    Place getPlaceByReservationId(Integer reservationId);
}
