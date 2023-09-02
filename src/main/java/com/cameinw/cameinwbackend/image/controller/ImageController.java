package com.cameinw.cameinwbackend.image.controller;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.image.model.Image;
import com.cameinw.cameinwbackend.image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/") //CHECK OK
    public List<Image> getAllImages() {
        return imageService.getAllImages();
    }

    @GetMapping("/users/{user_id}") //CHECK OK
    public ResponseEntity<byte[]> getUserImg(@PathVariable("user_id") Integer userId) {
        try {
            byte[] image = imageService.getUserImage(userId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/users/{user_id}") //CHECK OK
    public ResponseEntity<String> uploadUserImage(
            @PathVariable("user_id") Integer userId,
            @RequestParam("image") MultipartFile imgFile) {
        try {
            imageService.uploadUserImage(userId, imgFile);
            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed.");
        }
    }

    @PutMapping("/users/{user_id}")
    public ResponseEntity<String> updateUserImage(
            @PathVariable("user_id") Integer userId,
            @RequestParam("image") MultipartFile imgFile) {
        try {
            imageService.updateUserImage(userId, imgFile);
            return ResponseEntity.ok("Image updated successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image update failed.");
        }
    }

    @GetMapping("/places/{place_id}") //CHECK OK
    public ResponseEntity<List<Image>> getImagesByPlaceId(@PathVariable("place_id") Integer placeId) {
        Optional<List<Image>> images = imageService.getImagesByPlaceId(placeId);
        return images.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()); // STATUS: 404 Not Found
    }

    @PostMapping("/places/{place_id}") //CHECK OK
    public ResponseEntity<?> uploadImagesForPlace(
            @PathVariable("place_id") Integer placeId,
            @RequestParam("images") MultipartFile[] imgFiles
    ) {
        try {
            imageService.uploadImagesForPlace(placeId, imgFiles);
            return ResponseEntity.ok("Images uploaded successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while processing images.");
        }
    }

    @GetMapping("/places/{place_id}/{image_id}") //CHECK OK
    public ResponseEntity<byte[]> getPlaceImageById(@PathVariable("image_id") Integer imageId) {
        try {
            return imageService.getPlacesImage(imageId);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/places/{place_id}/{image_id}") //CHECK OK
    public ResponseEntity<?> deletePlaceImageById(@PathVariable("image_id") Integer imageId) {
        try {
            imageService.removePlacesImage(imageId);
            return ResponseEntity.noContent().build(); // STATUS: 204 No Content
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // STATUS: 404 Not Found
        }
    }
}
