package com.cameinw.cameinwbackend.place.request;

import com.cameinw.cameinwbackend.place.enums.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceRequest {
    @NotBlank(message = "Name cannot be null or empty.")
    private String name;
    @NotNull(message = "Property type cannot be null.")
    private PropertyType propertyType;
    private String description;
    @NotNull(message = "Cost cannot be null.")
    @PositiveOrZero(message = "Cost cannot be negative.")
    private Integer cost;
    @NotBlank(message = "Country cannot be null or empty.")
    private String country;
    @NotBlank(message = "City cannot be null or empty.")
    private String city;
    @NotBlank(message = "Address cannot be null or empty.")
    private String address;
    @NotNull(message = "Latitude cannot be null.")
    private Double latitude;
    @NotNull(message = "Longitude cannot be null.")
    private Double longitude;
    @NotNull(message = "Area cannot be null.")
    @PositiveOrZero(message = "Area cannot be negative.")
    private Integer area;
    @NotNull(message = "Guests cannot be null.")
    @PositiveOrZero(message = "Guests number cannot be negative.")
    private Integer guests;
    @NotNull(message = "Bedrooms cannot be null.")
    @PositiveOrZero(message = "Bedrooms number cannot be negative.")
    private Integer bedrooms;
    @NotNull(message = "Beds cannot be null.")
    @PositiveOrZero(message = "Beds number cannot be negative.")
    private Integer beds;
    @NotNull(message = "Bedrooms cannot be null.")
    @PositiveOrZero(message = "Bedrooms number cannot be negative.")
    private Integer bathrooms;
    @NotNull(message = "User ID cannot be null.")
    private Integer userId;

    public void validate() {
        if (!isValidPropertyType(propertyType)) {
            throw new IllegalArgumentException("Invalid property type. Available property types are: " + getPropertyTypesList());        }
    }

    private boolean isValidPropertyType(PropertyType propertyType) {
        return Arrays.asList(PropertyType.values()).contains(propertyType);
    }

    private String getPropertyTypesList() {
        return Arrays.stream(PropertyType.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
