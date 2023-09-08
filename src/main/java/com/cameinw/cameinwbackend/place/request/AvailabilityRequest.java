package com.cameinw.cameinwbackend.place.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityRequest {
    @NotNull(message = "Check in cannot be null.")
    private Date checkIn;
    @NotNull(message = "Check out cannot be null.")
    private Date checkOut;
    @NotBlank(message = "Country time cannot be null or empty.")
    private String country;
    @NotBlank(message = "City time cannot be null or empty.")
    private String city;
    @NotNull(message = "Latitude method cannot be null.")
    private Double latitude;
    @NotNull(message = "Longitude method cannot be null.")
    private Double longitude;
}