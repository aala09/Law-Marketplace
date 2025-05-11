package tn.esprit.cloud_in_mypocket.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileUtils {

    // Set of allowed file extensions
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
            Arrays.asList("pdf", "doc", "docx", "txt", "jpg", "jpeg", "png")
    );

    // Maximum file size (10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * Validates if a file is acceptable for upload
     * 
     * @param file The file to validate
     * @return true if file is valid, false otherwise
     */
    public static boolean isValidFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            return false;
        }
        
        // Check file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }
        
        String extension = getFileExtension(originalFilename);
        return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
    }
    
    /**
     * Extracts file extension from filename
     * 
     * @param filename The filename
     * @return The extension without the dot
     */
    public static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(dotIndex + 1);
    }
    
    /**
     * Safely reads a MultipartFile into a byte array
     * 
     * @param file The MultipartFile to read
     * @return The file content as byte array
     * @throws IOException if reading fails
     */
    public static byte[] getFileBytes(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return new byte[0];
        }
        return file.getBytes();
    }
}
