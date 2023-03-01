package com.chatop.estate.services;

import java.io.*;
import java.nio.file.*;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadServices {
    /**
     * Save file to disk
     * @param uploadDir
     * @param fileName
     * @param multipartFile
     * @throws IOException
     */
    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    /**
     * Remove file from disk
     * @param uploadDir
     * @param fileName
     * @throws IOException
     */
    public static void removeFile(String uploadDir, String fileName) throws IOException {
        try {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            throw new IOException("Invalid path: " + uploadDir);
        }


            Path filePath = uploadPath.resolve(fileName);
            Files.delete(filePath);
        } catch (IOException ioe) {
            throw new IOException("Could not remove image file: " + fileName, ioe);
        }
    }
}