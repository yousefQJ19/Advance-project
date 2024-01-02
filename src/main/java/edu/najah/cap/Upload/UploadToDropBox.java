package edu.najah.cap.Upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadToDropBox implements IUploadData {
    private static final Logger logger = LoggerFactory.getLogger(UploadToDropBox.class);

    private static Map<String, List<String>> DropBoxFiles = new HashMap<>();
    @Override
    public void Send(String email) {
        try{
            Thread.sleep(100);
            System.out.println("uploading......");
            if (DropBoxFiles.containsKey(email)) {
                List<String> existingFiles = DropBoxFiles.get(email);
                existingFiles.add("localHost:3000");
            } else {
                List<String> newFiles = new ArrayList<>();
                newFiles.add("localHost:3000");
                DropBoxFiles.put(email, newFiles);
            }
            logger.info("uploded the files to Dropbox for the email :{}",email);
        } catch (InterruptedException e) {
            logger.error("problem during uploading to dropbox occurred");
            throw new RuntimeException(e);
        }
    }
}