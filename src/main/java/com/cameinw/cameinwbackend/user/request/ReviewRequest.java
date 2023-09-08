package com.cameinw.cameinwbackend.user.request;

import com.cameinw.cameinwbackend.user.enums.PropertyRating;
import com.cameinw.cameinwbackend.user.model.User;
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
public class ReviewRequest {
    @NotNull(message = "Rating number cannot be negative.")
    private PropertyRating rating;
    private String comment;
    @NotNull(message = "User cannot be null.")
    private User user;

    public void validate() {
        if (!isValidRating(rating)) {
            throw new IllegalArgumentException("Invalid score. Available property ratings are: " + getPropertyRatingList());        }
    }

    private boolean isValidRating(PropertyRating propertyRating) {
        return Arrays.asList(PropertyRating.values()).contains(propertyRating);
    }

    private String getPropertyRatingList() {
        return Arrays.stream(PropertyRating.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}


// ------------------------ EXAMPLES -----------------------

//           --Create Review--
//
//{
//    "rating": "FIVE_STARS",
//    "user": {
//        "id": 2
//            }
//}

//           --Update Review--
//
//{
//    "comment": "Excellent apartment!"
//}
