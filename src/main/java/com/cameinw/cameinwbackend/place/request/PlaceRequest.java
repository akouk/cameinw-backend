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
    private Integer area;
    private Integer guests;
    private Integer bedrooms;
    private Integer beds;
    private Integer bathrooms;
    private Integer userId;
}
