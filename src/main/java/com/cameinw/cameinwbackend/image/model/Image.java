package com.cameinw.cameinwbackend.image.model;

import com.cameinw.cameinwbackend.place.model.Place;
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
@Table(name="images")

public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "imageName")
    private String imageName;


    @ManyToOne
    @JoinColumn(name = "placeId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Place place;

    @OneToOne
    @JoinColumn(name = "userId", unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
