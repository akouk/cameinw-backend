package com.cameinw.cameinwbackend.place.repository;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.model.Regulation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegulationRepository extends JpaRepository<Regulation, Integer> {
    Optional<Regulation> findById(Integer id);
    Optional<Regulation> findByPlaceId(Integer placeId);
    boolean existsByPlaceId(Integer placeId);
}
