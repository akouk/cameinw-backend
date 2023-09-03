package com.cameinw.cameinwbackend.place.service_impl;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.request.RegulationRequest;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.repository.RegulationRepository;
import com.cameinw.cameinwbackend.place.service.RegulationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegulationServiceImplementation implements RegulationService {
    private final RegulationRepository regulationRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public RegulationServiceImplementation(RegulationRepository regulationRepository, PlaceRepository placeRepository) {
        this.regulationRepository = regulationRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Regulation> getAllRegulations() {
        return regulationRepository.findAll();
    }

    @Override
    public Regulation getRegulationByPlaceId(Integer placeId) {
        return regulationRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Regulation not found for the specified place."));
    }

    @Override
    @Transactional
    public Regulation createRegulation(Integer placeId, RegulationRequest regulationRequest) {
        regulationRequest.validate();

        // Check if the user is the owner of the place
        Place place = getPlaceById(placeId);

        // Verify ownership
        checkUserOwnership(place, regulationRequest.getUserId());

        // Check if a regulation already exists for the place
        if (hasExistingRegulation(placeId)) {
            throw new CustomUserFriendlyException("A regulation already exists for this place. You can only update it.");
        }

        Regulation regulation = new Regulation();
        BeanUtils.copyProperties(regulationRequest, regulation);
        regulation.setPlace(place);

        return regulationRepository.save(regulation);
    }

    @Override
    @Transactional
    public Regulation updateRegulation(Integer placeId, Integer regulationId, RegulationRequest regulationRequest) {
        regulationRequest.validate();

        Place place = getPlaceById(placeId);
        Regulation existingRegulation = getRegulationById(regulationId);

        checkUserOwnership(place, regulationRequest.getUserId());

         BeanUtils.copyProperties(regulationRequest, existingRegulation);
        existingRegulation.setPlace(place);

        return regulationRepository.save(existingRegulation);
    }

    @Override
    @Transactional
    public void deleteRegulation(Integer placeId, Integer regulationId) {
        getPlaceById(placeId);
        Regulation regulation = getRegulationById(regulationId);
        regulationRepository.delete(regulation);
    }

    private Regulation getRegulationById(Integer regulationId) {
        return regulationRepository.findById(regulationId)
                .orElseThrow(() -> new ResourceNotFoundException("Regulation not found."));
    }

    private Place getPlaceById(Integer placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
    }

    private void checkUserOwnership(Place place, Integer userId) {
        if (!isUserOwnerOfPlace(place, userId)) {
            throw new CustomUserFriendlyException("User is not the owner of the place.");
        }
    }

    private boolean isUserOwnerOfPlace(Place place, Integer userId) {
        User placeOwner = place.getUser();
        return placeOwner != null && placeOwner.getId().equals(userId);
    }

    @Override
    public boolean hasExistingRegulation(Integer placeId) {
        return regulationRepository.existsByPlaceId(placeId);
    }
}
