package com.cameinw.cameinwbackend.place.model;

import com.cameinw.cameinwbackend.place.enums.PaymentMethod;
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
@Table(name="regulations")

public class Regulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="regulation_id")
    private Integer id;

    @Column(name = "arrival_time")
    private String arrivalTime;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "cancellation_policy")
    private String cancellationPolity;

    @Column(name = "payment_methods")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "age_restriction")
    private boolean ageRestriction;

    @Column(name = "pets_allowed")
    private boolean arePetsAllowed;

    @Column(name = "events_allowed")
    private boolean areEventsAllowed;

    @Column(name = "smoking_allowed")
    private boolean smokingAllowed;

    @Column(name = "quiet_hours")
    private String quietHours;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @OneToOne
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Place place;
}
