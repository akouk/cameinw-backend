package com.cameinw.cameinwbackend.image.repository;

import com.cameinw.cameinwbackend.image.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer>{
    Optional<List<Image>> findByPlaceId(Integer placeId);
}
