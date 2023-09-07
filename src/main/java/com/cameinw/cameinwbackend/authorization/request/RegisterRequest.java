package com.cameinw.cameinwbackend.authorization.request;

import com.cameinw.cameinwbackend.place.enums.PaymentMethod;
import com.cameinw.cameinwbackend.user.enums.Role;
import com.cameinw.cameinwbackend.place.model.Place;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username cannot be null or empty.")
    private  String theUserName;
    @NotBlank(message = "First name cannot be null or empty.")
    private String firstName;
    @NotBlank(message = "Last name be null or empty.")
    private String lastName;
    @NotBlank(message = "Password cannot be null or empty.")
    private String password;
    @NotBlank(message = "Email cannot be null or empty.")
    private String email;
    private Role role;
    @NotBlank(message = "Phone number cannot be null or empty.")
    private String phoneNumber;

    public void validate() {
        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role. Available roles are: " + getRolesList());        }
    }

    private boolean isValidRole(Role role) {
        return Arrays.asList(Role.values()).contains(role);
    }

    private String getRolesList() {
        return Arrays.stream(Role.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
