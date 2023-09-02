package com.cameinw.cameinwbackend.image.service_impl;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.image.model.Image;
import com.cameinw.cameinwbackend.image.repository.ImageRepository;
import com.cameinw.cameinwbackend.image.service.ImageService;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.image.utils.ImageHandler;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service

public class ImageServiceImplementation implements ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public ImageServiceImplementation(ImageRepository imageRepository, UserRepository userRepository, PlaceRepository placeRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public byte[] getUserImage(Integer userId) throws IOException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            byte[] image = ImageHandler.fetchUserImageBytes(user.get());
            return image;
        }
        throw new ResourceNotFoundException("User not found with ID: " + userId);
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
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("PlaceID " + placeId + " not found"));

        String imgName = imgFile.getOriginalFilename();

        Image image = new Image();
        image.setImageName(imgName);
        image.setPlace(place);
        imageRepository.save(image);

        try {
            ImageHandler.createPlacesImageDirectory(placeId);
            ImageHandler.saveImage("places/" + placeId + "/" + imgName, imgFile.getBytes());
        } catch (IOException e) {
            throw new CustomUserFriendlyException("Error while saving the image.");
        }
    }

    @Override
    @Transactional
    public void uploadUserImage(Integer userId, MultipartFile imgFile) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        String imgName = imgFile.getOriginalFilename();

        ImageHandler.createUsersImageDirectory(userId);
        ImageHandler.saveImage("users/" + userId + "/" + imgName, imgFile.getBytes());

        user.setImageName(imgName);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserImage(Integer userId, MultipartFile imgFile) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Delete the previous image if it exists
        if (user.getImageName() != null) {
            ImageHandler.deleteUserImage(userId, user.getImageName());
        }

        String imgName = imgFile.getOriginalFilename();

        ImageHandler.createUsersImageDirectory(userId);
        ImageHandler.saveImage("users/" + userId + "/" + imgName, imgFile.getBytes());

        user.setImageName(imgName);
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<byte[]> getPlacesImage(Integer imageId) throws IOException {
        Image img = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image " + imageId + " not found"));

        try {
            byte[] imgData = ImageHandler.fetchImageBytes(img.getPlace(), img.getImageName());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imgData);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> removePlacesImage(Integer imageId) {
        imageRepository.findById(imageId).ifPresent(image -> {
            String imgPath = "images/" + image.getPlace().getId() + "/" + image.getImageName();
            ImageHandler.deleteImage(imgPath);
            imageRepository.delete(image);
        });

        return ResponseEntity.ok().build();
    }
}
