package com.cameinw.cameinwbackend.place.service;

import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.request.RegulationRequest;

import java.util.List;

public interface RegulationService {
    List<Regulation> getAllRegulations();
    Regulation getRegulationByPlaceId(Integer placeId);
    Regulation createRegulation(Integer placeId, RegulationRequest regulationRequest);
    Regulation updateRegulation(Integer placeId, Integer regulationId, RegulationRequest regulationRequest);
    void deleteRegulation(Integer placeId, Integer regulationId);
    boolean hasExistingRegulation(Integer placeId);
}
