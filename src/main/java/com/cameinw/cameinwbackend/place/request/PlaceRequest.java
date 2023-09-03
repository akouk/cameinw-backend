package com.cameinw.cameinwbackend.place.request;

import com.cameinw.cameinwbackend.place.enums.PropertyType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceRequest {
    private String name;
    private PropertyType propertyType;
    private String description;
    private Integer cost;
    private String country;
    private String city;
    private String address;
    private double latitude;
    private double longitude;
    private Integer area;
    private Integer guests;
    private Integer bedrooms;
    private Integer beds;
    private Integer bathrooms;
    private Integer userId;
}

//Json example:
//{
//        "name": "Sample Place",
//        "propertyType": "APARTMENT",
//        "description": "A beautiful apartment in the city center",
//        "cost": 100,
//        "country": "Greece",
//        "city": "Athens",
//        "address": "123 Main Street",
//        "latitude": 40.7128,
//        "longitude": -74.0060,
//        "area": 100,
//        "guests": 4,
//        "bedrooms": 2,
//        "beds": 2,
//        "bathrooms": 2,
//        "userId": 1
//        }

