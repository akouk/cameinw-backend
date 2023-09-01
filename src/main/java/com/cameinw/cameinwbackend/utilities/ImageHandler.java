package com.cameinw.cameinwbackend.utilities;

import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.place.model.Place;
import lombok.Getter;

import java.io.IOException;

public class ImageHandler {
    @Getter
    private static String mainPath;

    public static void setMainPath(String mainPath) {
        ImageHandler.mainPath = mainPath;
    }

    public static boolean createPlacesImageDirectory(Integer placeId) {
        String directoryPath = mainPath + "places/" + placeId;
        return FileUtils.makeDirectory(directoryPath);
    }

    public static boolean createUsersImageDirectory(Integer userId) {
        String directoryPath = mainPath + "users/" + userId;
        return FileUtils.makeDirectory(directoryPath);
    }

    public static void saveImage(String imgName, byte[] imgContent) throws IOException {
        String imgPath = mainPath + imgName;
        FileUtils.writeBytesToFile(imgPath, imgContent);
    }

    public static byte[] fetchImageBytes(Place place, String imgName) throws IOException {
        String imgPath = ImageHandler.getMainPath() + "places/" + place.getId() + "/" + imgName;
        return FileUtils.readBytesFromFile(imgPath);
    }

    public static boolean deleteImage(String imgPath) {
        return FileUtils.deleteFile(imgPath);
    }

    public static byte[] fetchUserImageBytes(User user) throws IOException {
        String imgName = user.getImageName();
        String imgPath = ImageHandler.getMainPath() + "users/" + user.getId() + "/" + imgName;

        return FileUtils.readBytesFromFile(imgPath);
    }

    public static boolean deleteUserImage(Integer userId, String imageName) {
        String imgPath = mainPath + "users/" + userId + "/" + imageName;
        return FileUtils.deleteFile(imgPath);
    }
}
