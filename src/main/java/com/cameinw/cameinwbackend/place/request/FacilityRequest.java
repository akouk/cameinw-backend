package com.cameinw.cameinwbackend.place.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FacilityRequest {
    private boolean hasFreeParking;
    private boolean isNonSmoking;
    private boolean hasFreeWiFi;
    @NotNull(message = "Breakfast cannot be null.")
    private Boolean hasbreakfast;
    private boolean hasbalcony;
    private boolean hasSwimmingPool;
    @NotNull(message = "User ID cannot be null.")
    private Integer userId;
}
