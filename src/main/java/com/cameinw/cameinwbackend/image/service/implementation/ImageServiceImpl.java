package com.cameinw.cameinwbackend.image.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.image.model.Image;
import com.cameinw.cameinwbackend.image.repository.ImageRepository;
import com.cameinw.cameinwbackend.image.service.ImageService;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.image.utils.ImageHandler;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service

public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, UserRepository userRepository, PlaceRepository placeRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    private static final String IMAGES_PATH = "images/";
    private static final String USER_IMAGE_PATH = "users/";
    private static final String PLACES_IMAGE_PATH = "places/";

    @Override
    public List<Image> getAllImages() {

        List<Image> images = imageRepository.findAll();

        if (images.isEmpty()) {
            throw new ResourceNotFoundException("No images were found.");
        }
        return images;
    }

    @Override
    public byte[] getUserImage(Integer userId) {
        User user = getUserById(userId);
        try {
            return  ImageHandler.fetchUserImageBytes(user);
        } catch (IOException e) {
        throw new CustomUserFriendlyException("Error while fetching the image.");
        }
    }

    @Override
    public Optional<List<Image>> getImagesByPlaceId(Integer placeId) {
        return imageRepository.findByPlaceId(placeId);
    }

    @Override
    @Transactional
    public void uploadImagesForPlace(Integer placeId, MultipartFile[] imageFiles) {
        for (MultipartFile imageFile : imageFiles) {
            saveSingleImage(placeId, imageFile);
        }
    }

    private void saveSingleImage(Integer placeId, MultipartFile imgFile) {
        Place place = getPlaceById(placeId);

        String imgName = imgFile.getOriginalFilename();

        Image image = new Image();
        image.setImageName(imgName);
        image.setPlace(place);
        imageRepository.save(image);

        try {
            ImageHandler.createPlacesImageDirectory(placeId);
            ImageHandler.saveImage(PLACES_IMAGE_PATH + placeId + "/" + imgName, imgFile.getBytes());
        } catch (IOException e) {
            throw new CustomUserFriendlyException("Error while saving the image.");
        }
    }

    @Override
    @Transactional
    public void uploadUserImage(Integer userId, MultipartFile imgFile) {
        User user = getUserById(userId);

        String imgName = imgFile.getOriginalFilename();

        try {
            ImageHandler.createUsersImageDirectory(userId);
            ImageHandler.saveImage(USER_IMAGE_PATH + userId + "/" + imgName, imgFile.getBytes());
        } catch (IOException ex) {
            throw new CustomUserFriendlyException("Error while saving the image.");
        }

        user.setImageName(imgName);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserImage(Integer userId, MultipartFile imgFile) {
        User user = getUserById(userId);

        // Delete the previous image if it exists
        if (user.getImageName() != null) {
            ImageHandler.deleteUserImage(userId, user.getImageName());
        }

        String imgName = imgFile.getOriginalFilename();

        try {
            ImageHandler.createUsersImageDirectory(userId);
            ImageHandler.saveImage(USER_IMAGE_PATH + userId + "/" + imgName, imgFile.getBytes());
        } catch (IOException ex) {
            throw new CustomUserFriendlyException("Error while saving the image.");
        }

        user.setImageName(imgName);
        userRepository.save(user);
    }

    @Override
    public byte[] getPlacesImage(Integer placeId, Integer imageId) {
        Place place = getPlaceById(placeId);
        Image img = getImageById(imageId);

        if (!img.getPlace().equals(place)) {
            throw new ResourceNotFoundException("Image is not associated with the Place.");
        }

        try {
            return ImageHandler.fetchImageBytes(img.getPlace(), img.getImageName());
        } catch (IOException e) {
            throw new CustomUserFriendlyException("Error while fetching the image.");
        }

    }

    @Override
    @Transactional
    public void deletePlacesImage(Integer placeId, Integer imageId) {
        Place place = getPlaceById(placeId);
        Image img = getImageById(imageId);

        if (!img.getPlace().equals(place)) {
            throw new ResourceNotFoundException("Image is not associated with the Place.");
        }

        String imgPath = IMAGES_PATH + img.getPlace().getId() + "/" + img.getImageName();
        ImageHandler.deleteImage(imgPath);
        imageRepository.delete(img);
    }

    private Image getImageById(Integer imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    private Place getPlaceById(Integer placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
    }
}
