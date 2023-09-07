package com.cameinw.cameinwbackend.place.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceAlreadyExistException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.request.RegulationRequest;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.repository.RegulationRepository;
import com.cameinw.cameinwbackend.place.service.RegulationService;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegulationServiceImpl implements RegulationService {
    private final RegulationRepository regulationRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    @Autowired
    public RegulationServiceImpl(RegulationRepository regulationRepository, PlaceRepository placeRepository, UserRepository userRepository) {
        this.regulationRepository = regulationRepository;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Regulation> getAllRegulations() {
        List<Regulation> regulations = regulationRepository.findAll();
        checkIfRegulationsWereFound(regulations);
        return regulations;
    }

    @Override
    public Regulation getRegulationByPlaceId(Integer placeId) {
        Regulation regulations = regulationRepository.findByPlaceId(placeId);
        checkIfRegulationsWereFoundForThePlace(regulations);
        return regulations;
    }

    @Override
    @Transactional
    public Regulation createRegulation(Integer placeId, RegulationRequest regulationRequest) {
        Place place = getPlaceById(placeId);
        User user = getUserById(regulationRequest.getUser().getId());
        checkUserOwnership(place, user.getId());

        checkIfRegulationsExist(placeId);
        Regulation regulation = createNewRegulation(regulationRequest, place);
        return saveRegulation(regulation);
    }

    @Override
    @Transactional
    public Regulation updateRegulation(Integer placeId, Integer regulationId, RegulationRequest regulationRequest) {
        User user = getUserById(regulationRequest.getUser().getId());
        Place place = getPlaceById(placeId);
        checkUserOwnership(place, user.getId());

        Regulation regulations = getRegulationById(regulationId);
        copyOnlyNonNullProperties(regulationRequest, regulations);
        return saveRegulation(regulations);
    }

    @Override
    @Transactional
    public void deleteRegulation(Integer placeId, Integer regulationId) {
        Place place = getPlaceById(placeId);
        Regulation regulation = getRegulationById(regulationId);
        regulationRepository.delete(regulation);
    }

    private void checkIfRegulationsWereFound(List<Regulation> regulations) {
        if (regulations.isEmpty()) {
            throw new ResourceNotFoundException("No regulations were found.");
        }
    }

    private void checkIfRegulationsWereFoundForThePlace(Regulation regulation) {
        if (regulation == null) {
            throw new ResourceNotFoundException("Regulation not found for the specified place.");
        }
    }

    private Regulation getRegulationById(Integer regulationId) {
        return regulationRepository.findById(regulationId)
                .orElseThrow(() -> new ResourceNotFoundException("Regulation not found."));
    }

    private Place getPlaceById(Integer placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
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

    private void checkIfRegulationsExist(Integer placeId) {
        if (hasExistingRegulation(placeId)) {
            throw new ResourceAlreadyExistException("A regulation already exists for this place. You can only update it.");
        }
    }

    private Regulation createNewRegulation(RegulationRequest regulationRequest, Place place) {
        regulationRequest.validate();
        return Regulation.builder()
                .arrivalTime(regulationRequest.getArrivalTime())
                .departureTime(regulationRequest.getDepartureTime())
                .cancellationPolity(regulationRequest.getCancellationPolicy())
                .paymentMethod(regulationRequest.getPaymentMethod())
                .ageRestriction(regulationRequest.isAgeRestriction())
                .arePetsAllowed(regulationRequest.isArePetsAllowed())
                .areEventsAllowed(regulationRequest.isAreEventsAllowed())
                .smokingAllowed(regulationRequest.isSmokingAllowed())
                .quietHours(regulationRequest.getQuietHours())
                .place(place)
                .build();
    }

    private void copyOnlyNonNullProperties(RegulationRequest regulationRequest, Regulation regulation) {
        if (regulationRequest.getArrivalTime() != null) {
            regulation.setArrivalTime(regulationRequest.getArrivalTime());
        }
        if (regulationRequest.getDepartureTime() != null) {
            regulation.setDepartureTime(regulationRequest.getDepartureTime());
        }
        if (regulationRequest.getCancellationPolicy() != null) {
            regulation.setCancellationPolity(regulationRequest.getCancellationPolicy());
        }
        if (regulationRequest.getPaymentMethod() != null) {
            regulation.setPaymentMethod(regulationRequest.getPaymentMethod());
        }
        if (regulationRequest.isAgeRestriction() != false) {
            regulation.setAgeRestriction(regulationRequest.isAgeRestriction());
        }
        if (regulationRequest.isArePetsAllowed() != false) {
            regulation.setArePetsAllowed(regulationRequest.isArePetsAllowed());
        }
        if (regulationRequest.isAreEventsAllowed() != false) {
            regulation.setAreEventsAllowed(regulationRequest.isAreEventsAllowed());
        }
        if (regulationRequest.isSmokingAllowed() != false) {
            regulation.setSmokingAllowed(regulationRequest.isSmokingAllowed());
        }
        if (regulationRequest.getQuietHours() != null) {
            regulation.setQuietHours(regulationRequest.getQuietHours());
        }

    }

    private Regulation saveRegulation(Regulation regulation) {
        return regulationRepository.save(regulation);
    }
}
