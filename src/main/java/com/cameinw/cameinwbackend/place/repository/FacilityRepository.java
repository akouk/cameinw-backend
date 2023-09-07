package com.cameinw.cameinwbackend.place.repository;

import com.cameinw.cameinwbackend.place.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Integer> {
    Optional<Facility> findById(Integer facilityId);
    boolean existsByPlaceId(Integer placeId);
    Facility findByPlaceId(Integer placeId);
}
