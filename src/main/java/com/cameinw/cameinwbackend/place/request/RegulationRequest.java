package com.cameinw.cameinwbackend.place.request;

import com.cameinw.cameinwbackend.place.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class RegulationRequest {
    @NotBlank(message = "Arrival time cannot be null or empty.")
    private String arrivalTime;
    @NotBlank(message = "Departure time cannot be null or empty.")
    private String departureTime;
    private String cancellationPolicy;
    @NotNull(message = "Payment method cannot be null.")
    private PaymentMethod paymentMethod;
    private boolean ageRestriction;
    private boolean arePetsAllowed;
    private boolean areEventsAllowed;
    private boolean smokingAllowed;
    private String quietHours;
    @NotNull(message = "User ID cannot be null.")
    private Integer userId;

    public void validate() {
        if (!isValidPaymentMethode(paymentMethod)) {
            throw new IllegalArgumentException("Invalid payment method. Available payment methods are: " + getPaymentMethodsList());        }
    }

    private boolean isValidPaymentMethode(PaymentMethod paymentMethod) {
        return Arrays.asList(PaymentMethod.values()).contains(paymentMethod);
    }

    private String getPaymentMethodsList() {
        return Arrays.stream(PaymentMethod.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
