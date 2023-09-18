package com.cameinw.cameinwbackend.image.service;

import com.cameinw.cameinwbackend.image.model.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ImageService {
    List<Image> getAllImages();
    ResponseEntity<byte[]> getUserImage(Integer userId) throws IOException;
    void uploadUserImage(Integer userId, MultipartFile imgFile) throws IOException;
    void updateUserImage(Integer userId, MultipartFile imgFile) throws IOException;
    Optional<List<Image>> getImagesByPlaceId(Integer placeId);
    void uploadImagesForPlace(Integer placeId, MultipartFile[] imageFiles) throws IOException;
    void uploadMainImageForPlace(Integer userId, MultipartFile imgFile) throws IOException;
    public void updateMainImageForPlace(Integer placeId,  MultipartFile imgFile) throws IOException;
    ResponseEntity<byte[]> getPlacesImage(Integer placeId, Integer imageId) throws IOException;
    ResponseEntity<byte[]> getPlacesMainImage(Integer placeId) throws IOException;
    void deletePlacesImage(Integer placeId, Integer imageId);
}
