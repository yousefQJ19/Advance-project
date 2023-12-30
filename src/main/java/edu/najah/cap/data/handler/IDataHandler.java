package edu.najah.cap.data.handler;

import java.io.IOException;

public interface IDataHandler {
    void exportUserData(String userId, String storagePath) throws IOException;
    void deleteUserData(String userId, boolean hardDelete);
    void convertToPdf(String inputFilePath, String outputFilePath) throws IOException;
}
