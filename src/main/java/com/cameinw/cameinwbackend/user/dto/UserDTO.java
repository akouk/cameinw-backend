package com.cameinw.cameinwbackend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    Integer userId;
    String username;
    String email;
    String firstName;
    String lastName;
    String phone;
}
