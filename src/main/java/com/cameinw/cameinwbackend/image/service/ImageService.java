package com.cameinw.cameinwbackend.image.service;

import com.cameinw.cameinwbackend.image.model.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ImageService {
    List<Image> getAllImages();
    byte[] getUserImage(Integer userId) throws IOException;
    void uploadUserImage(Integer userId, MultipartFile imgFile) throws IOException;
    void updateUserImage(Integer userId, MultipartFile imgFile) throws IOException;
    Optional<List<Image>> getImagesByPlaceId(Integer placeId);
    void uploadImagesForPlace(Integer placeId, MultipartFile[] imageFiles);
    ResponseEntity<byte[]> getPlacesImage(Integer imageId) throws IOException;
    ResponseEntity<?> removePlacesImage(Integer imageId);
}
