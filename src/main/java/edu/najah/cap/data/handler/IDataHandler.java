package edu.najah.cap.data.handler;

import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;

import java.io.IOException;

public interface IDataHandler {
    void exportUserData(String userId, String storagePath) throws IOException, SystemBusyException, NotFoundException, BadRequestException;
    void deleteUserData(String userId, boolean hardDelete) throws SystemBusyException, NotFoundException, BadRequestException;
    void convertToPdf(String inputFilePath, String outputFilePath) throws IOException;
}
