package com.cameinw.cameinwbackend.user.request;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    @NotNull(message = "Score cannot be null.")
    @PositiveOrZero(message = "Score number cannot be negative.")
    private Integer score;
    @NotBlank(message = "Comment cannot be null or empty.")
    private String comment;
    @NotNull(message = "User cannot be null.")
    private User user;
    @NotNull(message = "Place cannot be null.")
    private Place place;
}
