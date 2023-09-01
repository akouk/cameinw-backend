package com.cameinw.cameinwbackend.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static boolean makeDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return false;
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    public static void writeBytesToFile(String filePath, byte[] content) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, content);
    }

    public static byte[] readBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        return org.apache.commons.io.FileUtils.readFileToByteArray(file);
    }
}
