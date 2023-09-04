package com.cameinw.cameinwbackend.authorization.request;

import com.cameinw.cameinwbackend.user.enums.Role;
import com.cameinw.cameinwbackend.place.model.Place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private  String theUserName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Role role;
    private String phoneNumber;
}
