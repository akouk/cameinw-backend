package com.cameinw.cameinwbackend.place.projection;

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
    Enum getPropertyType();
}
