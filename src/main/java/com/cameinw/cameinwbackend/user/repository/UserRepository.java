package com.cameinw.cameinwbackend.user.repository;

import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

//To JpaRepository έχει μεθόδους find all, find by id κλπ
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(Integer id);
    Optional<User> findByTheUserName(String theUserName);
    Optional<User> findByEmail(String email);


    /* Returns specific information about the user, in contrast to the findById method
     * that returns all the related information too, like places, reservations etc */
    @Query(value ="SELECT a.id, a.theusername, a.firstname, a.lastname, a.email, a.phonenumber " +
            "FROM _user a " +
            "WHERE a.id = ?1 ", nativeQuery = true)
    UserProjection findUserById(Integer id);
}