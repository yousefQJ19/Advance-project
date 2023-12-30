package edu.najah.cap.Upload;


import jakarta.mail.MessagingException;

import java.io.IOException;

public interface IUploadData {
    public void Send(String email) throws MessagingException, IOException;
}
