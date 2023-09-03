package com.cameinw.cameinwbackend.place.repository;

import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.model.Regulation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Integer> {
    Optional<Facility> findById(Integer id);
    boolean existsByPlaceId(Integer placeId);


}
