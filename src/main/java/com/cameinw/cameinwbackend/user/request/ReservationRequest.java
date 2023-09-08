package com.cameinw.cameinwbackend.user.request;

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
    @NotNull(message = "User cannot be null.")
    User user;
}

// ------------------------ EXAMPLE -----------------------

//           --Create Reservation--
//
//{
//    "checkIn": "2023-09-10",
//    "checkOut": "2023-09-15",
//    "user": {
//        "id": 2
//            }
//}
