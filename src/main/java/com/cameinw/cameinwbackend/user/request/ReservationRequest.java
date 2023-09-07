package com.cameinw.cameinwbackend.user.request;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.User;
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
public class ReservationRequest {
    @NotNull(message = "Check in cannot be null.")
    private Date checkIn;
    @NotNull(message = "Check out cannot be null.")
    private Date checkOut;
    @NotNull(message = "Place ID cannot be null.")
    Place place;
    @NotNull(message = "User ID cannot be null.")
    User user;
}
