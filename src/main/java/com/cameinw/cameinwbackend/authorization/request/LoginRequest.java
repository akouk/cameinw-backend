package com.cameinw.cameinwbackend.authorization.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email cannot be null or empty.")
    private String email;
    @NotBlank(message = "Password cannot be null or empty.")
    private String password;
}
