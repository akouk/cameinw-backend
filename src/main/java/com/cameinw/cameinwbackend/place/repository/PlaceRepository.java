package com.cameinw.cameinwbackend.place.repository;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.projection.PlaceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer>{
    Optional<Place> findById(Integer id);

    @Query(value ="SELECT a.id, a.theUserName, a.address, a.name, a.cost, a.guests " +
            "FROM places a " +
            "WHERE a.country = ?1 " +
            "AND a.city = ?2 " +
            "AND a.guests > ?3 ", nativeQuery = true)
    List<PlaceProjection> findPlacesByQuery(String country, String city, Integer guests);


    @Query(value ="SELECT a.id, a.name, a.country, a.city, a.address, a.cost, a.guests, a.bedrooms, a.bathrooms, a.type, a.description " +
            "FROM places a " +
            "WHERE a.user_id = ?1 ", nativeQuery = true)
    Optional<List<PlaceProjection>> findPlacesByUserId(Integer userId);

    @Query(value ="SELECT a " +
            "FROM Place a " +
            "WHERE a.country = ?1 " +
            "AND a.city = ?2 ")
    List<Place> findPlacesByCountryAndCity(String country, String city);

    @Query(value = "Select p FROM Place where p.latitude >= ?1 and p.latitude <= ?2 and p.longitude >= ?3 and p.longitude <= ?4 order by p.cost", nativeQuery = true)
    List<Place> getNearbyPlaces(double maxLat,
                                double minLat,
                                double maxLong,
                                double minLong);
}
