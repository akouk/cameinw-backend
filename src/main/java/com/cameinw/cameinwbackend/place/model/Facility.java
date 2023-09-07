package com.cameinw.cameinwbackend.place.model;

import com.cameinw.cameinwbackend.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="facilities")

public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "free_parking")
    private boolean hasFreeParking;

    @Column(name = "non_smoking")
    private boolean isNonSmoking;

    @Column(name = "free_WiFi")
    private boolean hasFreeWiFi;

    @Column(name = "breakfast")
    private boolean hasbreakfast;

    @Column(name = "balcony")
    private boolean hasbalcony;

    @Column(name = "swimming_pool")
    private boolean hasSwimmingPool;

//    @OneToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
//    private User user;

    @OneToOne
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Place place;
}
