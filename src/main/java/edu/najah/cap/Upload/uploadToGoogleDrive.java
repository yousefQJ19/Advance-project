package edu.najah.cap.Upload;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class uploadToGoogleDrive implements IUploadData{
    private static final Logger logger = LoggerFactory.getLogger(uploadToGoogleDrive.class);
    private static Map<String, List<String>> driveFiles = new HashMap<>();
    @Override
    public void Send(String email) {
        try{
            Thread.sleep(100);
            if (driveFiles.containsKey(email)) {
                List<String> existingFiles = driveFiles.get(email);
                existingFiles.add("localHost:3000");
            } else {
                List<String> newFiles = new ArrayList<>();
                newFiles.add("localHost:3000");
                driveFiles.put(email, newFiles);
            }
            logger.info("uploaded to google drive successfully for an the link sent to :{}",email);
        } catch (InterruptedException e) {
            logger.error("a problem cored in uploading the files");
            throw new RuntimeException(e);
        }

  
    }
}