package com.onboarding.actors.helper;

import java.io.File;

public class FilePersistenceHelper {

    public static void removeFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) { // Check if the file exists
            if (file.delete()) { // Attempt to delete the file
                System.out.println("File deleted successfully: " + filePath);
            } else {
                System.err.println("Failed to delete the file: " + filePath);
            }
        } else {
            System.err.println("File does not exist: " + filePath);
        }
    }
}
