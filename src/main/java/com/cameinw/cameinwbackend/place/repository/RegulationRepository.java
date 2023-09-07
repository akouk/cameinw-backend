package com.cameinw.cameinwbackend.place.repository;

import com.cameinw.cameinwbackend.place.model.Regulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegulationRepository extends JpaRepository<Regulation, Integer> {
    Optional<Regulation> findById(Integer regulationId);
    boolean existsByPlaceId(Integer placeId);
    Regulation findByPlaceId(Integer placeId);

}
