package com.cameinw.cameinwbackend.image.controller;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.image.model.Image;
import com.cameinw.cameinwbackend.image.service.ImageService;
import com.cameinw.cameinwbackend.place.model.Regulation;
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

    @GetMapping() //CHECK OK
    public ResponseEntity<?> getAllImages() {

        try {
            List<Image> images = imageService.getAllImages();
            return new ResponseEntity<>(images, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/users/{user_id}/image") //CHECK OK!!!!!
    public ResponseEntity<byte[]> getImageByUserId(@PathVariable("user_id") Integer userId) {
        try {
            return imageService.getUserImage(userId);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/users/{user_id}/image") //CHECK OK!!!!!
    public ResponseEntity<String> uploadUserImage(
            @PathVariable("user_id") Integer userId,
            @RequestParam("image") MultipartFile imgFile) {
        try {
            imageService.uploadUserImage(userId, imgFile);
            return ResponseEntity.status(HttpStatus.CREATED).body("Image uploaded successfully.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("/users/{user_id}/image") //-------check ok-----------
    public ResponseEntity<String> updateUserImage(
            @PathVariable("user_id") Integer userId,
            @RequestParam("image") MultipartFile imgFile) {
        try {
            imageService.updateUserImage(userId, imgFile);
            return ResponseEntity.status(HttpStatus.OK).body("Image updated successfully.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/places/{place_id}/gallery") //CHECK OK!!!!!
    public ResponseEntity<List<Image>> getImagesByPlaceId(@PathVariable("place_id") Integer placeId) {
        Optional<List<Image>> images = imageService.getImagesByPlaceId(placeId);
        return images.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()); // STATUS: 404 Not Found
    }

    @PostMapping("/places/{place_id}/gallery") //CHECK OK!!!!!
    public ResponseEntity<?> uploadImagesForPlace(
            @PathVariable("place_id") Integer placeId,
            @RequestParam("images") MultipartFile[] imgFiles
    ) {
        try {
            imageService.uploadImagesForPlace(placeId, imgFiles);
            return ResponseEntity.status(HttpStatus.OK).body("Images uploaded successfully.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/places/{place_id}/main-image") //CHECK OK!!!!!
    public ResponseEntity<?> uploadMainImageForPlace(
            @PathVariable("place_id") Integer placeId,
            @RequestParam("image") MultipartFile imgFile
    ) {
        try {
            imageService.uploadMainImageForPlace(placeId, imgFile);
            return ResponseEntity.status(HttpStatus.OK).body("Main image uploaded successfully.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("/places/{place_id}/main-image") //-------check ok-----------
    public ResponseEntity<String> updateMainImageForPlace(
            @PathVariable("place_id") Integer placeId,
            @RequestParam("image") MultipartFile imgFile) {
        try {
            imageService.updateMainImageForPlace(placeId, imgFile);
            return ResponseEntity.status(HttpStatus.OK).body("Image updated successfully.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/places/{place_id}/main-image") //CHECK OK!!!!!
    public ResponseEntity<byte[]> getPlacesMainImage(@PathVariable("place_id") Integer placeId) {
        try {
            return imageService.getPlacesMainImage(placeId);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/places/{place_id}/gallery/{image_id}") //CHECK OK!!!!!
    public ResponseEntity<byte[]> getPlaceImageById(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("image_id") Integer imageId
    ) {
        try {
            return imageService.getPlacesImage(placeId, imageId);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/places/{place_id}/gallery/{image_id}") //CHECK OK!!!!!
    public ResponseEntity<?> deletePlaceImageById(@PathVariable("place_id") Integer placeId, @PathVariable("image_id") Integer imageId) {
        try {
            imageService.deletePlacesImage(placeId, imageId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Place's image deleted successfully.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
