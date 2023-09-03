package com.cameinw.cameinwbackend.user.repository;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUser(User user);

    List<Reservation> findByPlace(Place place);

    @Override
    Optional<Reservation> findById(Integer reservationId);

    @Query(value ="SELECT a " +
            "FROM Reservation a " +
            "WHERE a.place = ?3 " +
            "AND ((a.checkIn<= ?1 " +
            "AND a.checkOut >= ?1 " +
            "OR a.checkIn <= ?2 " +
            "AND a.checkOut >= ?2) " +
            "OR (a.checkIn > ?1 " +
            "AND a.checkOut < ?2)) ")
    List<Reservation> findBetweenDates(Date checkIn, Date checkOut, Place place);
}
