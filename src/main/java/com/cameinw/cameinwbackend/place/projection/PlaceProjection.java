package com.cameinw.cameinwbackend.place.projection;

import com.cameinw.cameinwbackend.place.enums.PropertyType;

public interface PlaceProjection {
    Integer getId();
    String getName();
    String getCountry();
    String getCity();
    String getAddress();
    Integer getGuests();
    Integer getCost();
    Integer getBathrooms();
    Integer getBedrooms();
    String getDescription();
    Enum<PropertyType> getPropertyType();
}
