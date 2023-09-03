package com.cameinw.cameinwbackend.user.model;

import com.cameinw.cameinwbackend.image.model.Image;
import com.cameinw.cameinwbackend.user.enums.Role;
import com.cameinw.cameinwbackend.place.model.Place;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")

public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
    private Integer id;

    @Column(name="username", unique=true, nullable=false)
    private String theUserName;

    @Column(name="email", unique=true, nullable=false)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="phone")
    private String phoneNumber;

    @Column(name="image_name")
    private String imageName = "userImg.jpg";



    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    //@JsonIgnore
    private List<Place> places;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_id", referencedColumnName = "id")
    @JsonIgnore
    private List<Reservation> reservations;
//
//    @OneToMany(mappedBy = "user")
//    private List<Review> reviews;
//
//    @OneToMany(mappedBy = "user")
//    @JsonIgnore
//    private List<Search> searches;
//
    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<Message> sendedMessages;

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<Message> receivedMessages;

    @OneToOne(mappedBy = "user")
    private Image image;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", theUserName='" + theUserName + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                // We don't include the places, reservations, reviews, searches, sendedMessages, and receivedMessages fields here to avoid circular reference.
                // TODO: CHECK AGAIN LATER
                '}';
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
