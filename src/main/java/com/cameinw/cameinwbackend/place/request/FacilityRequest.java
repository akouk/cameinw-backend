package com.cameinw.cameinwbackend.place.request;

import jakarta.persistence.Column;
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
    private boolean hasbreakfast;
    private boolean hasbalcony;
    private boolean hasSwimmingPool;
    private Integer userId;
}
