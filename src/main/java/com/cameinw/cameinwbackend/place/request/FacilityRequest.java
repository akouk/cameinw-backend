package com.cameinw.cameinwbackend.place.request;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FacilityRequest {
    private boolean hasFreeParking;
    private boolean isNonSmoking;
    private boolean hasFreeWiFi;
    @NotNull(message = "Breakfast cannot be null.")
    private Boolean hasbreakfast;
    private boolean hasbalcony;
    private boolean hasSwimmingPool;
    @NotNull(message = "User cannot be null.")
    private User user;
}

// ------------------------ EXAMPLES -----------------------

//           --Create Facility--
//
//{
//    "hasbreakfast": true,
//    "user" : {
//        "id" : 2
//    }
//}

//           --Update Facility--
//
//{
//    "hasFreeWiFi" : true,
//    "hasbreakfast": true,
//    "user" : {
//        "id" : 2
//    }
//}
