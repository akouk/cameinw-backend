package com.cameinw.cameinwbackend.place.request;

import com.cameinw.cameinwbackend.place.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegulationRequest {
    private String arrivalTime;
    private String departureTime;
    private String cancellationPolicy;
    private PaymentMethod paymentMethod;
    private boolean ageRestriction;
    private boolean arePetsAllowed;
    private boolean areEventsAllowed;
    private boolean smokingAllowed;
    private String quietHours;
    private Integer userId;
}
