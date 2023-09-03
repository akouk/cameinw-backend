package com.cameinw.cameinwbackend.place.model;

import com.cameinw.cameinwbackend.image.model.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.place.enums.PropertyType;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "place_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Column(name = "description")
    private String description;

    @Column(name = "main_image")
    private String mainImage;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "area")
    private Integer area;

    @Column(name = "guests")
    private Integer guests;

    @Column(name = "bedrooms")
    private Integer bedrooms;

    @Column(name = "beds")
    private Integer beds;

    @Column(name = "bathrooms")
    private Integer bathrooms;


    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "place")
    private List<Image> images;

//    @OneToMany(mappedBy = "place")
//    private List<Review> reviews;
//
//    @OneToMany(mappedBy = "place")
//    private List<Availability> availabilities;
//
//    @OneToMany(mappedBy = "place")
//    @JsonIgnore
//    private List<Reservation> reservations;

    @OneToOne(mappedBy = "place")
    private Regulation regulations;
}
