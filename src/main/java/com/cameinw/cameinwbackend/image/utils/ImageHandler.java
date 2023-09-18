package com.cameinw.cameinwbackend.image.utils;

import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.utilities.FileUtils;
import lombok.Getter;

import java.io.IOException;

public class ImageHandler {
    private static final String USER_IMAGE_PATH = "users/";
    private static final String PLACES_IMAGE_PATH = "places/";

    @Getter
    private static String mainPath;

    public static void setMainPath(String mainPath) {
        ImageHandler.mainPath = mainPath;
    }

    public static boolean createPlacesImageDirectory(Integer placeId) {
        String directoryPath = mainPath + PLACES_IMAGE_PATH + placeId;
        return FileUtils.makeDirectory(directoryPath);
    }

    public static boolean createUsersImageDirectory(Integer userId) {
        String directoryPath = mainPath + USER_IMAGE_PATH + userId;
        return FileUtils.makeDirectory(directoryPath);
    }

    public static void saveImage(String imgName, byte[] imgContent) throws IOException {
        String imgPath = mainPath + imgName;
        FileUtils.writeBytesToFile(imgPath, imgContent);
    }

    public static byte[] fetchImageBytes(Place place, String imgName) throws IOException {
        String imgPath = ImageHandler.getMainPath() + PLACES_IMAGE_PATH + place.getId() + "/" + imgName;
        return FileUtils.readBytesFromFile(imgPath);
    }

    public static boolean deleteImage(String imgPath) {
        return FileUtils.deleteFile(imgPath);
    }

    public static byte[] fetchUserImageBytes(User user) throws IOException {
        String imgName = user.getImageName();
        String imgPath = ImageHandler.getMainPath() + USER_IMAGE_PATH + user.getId() + "/" + imgName;

        return FileUtils.readBytesFromFile(imgPath);
    }

    public static byte[] fetchPlacesMainImageBytes(Place place) throws IOException {
        String imgName = place.getMainImage();
        String imgPath = ImageHandler.getMainPath() + PLACES_IMAGE_PATH + place.getId() + "/" + imgName;

        return FileUtils.readBytesFromFile(imgPath);
    }

    public static boolean deleteUserImage(Integer userId, String imageName) {
        String imgPath = mainPath + USER_IMAGE_PATH + userId + "/" + imageName;
        return FileUtils.deleteFile(imgPath);
    }
}
