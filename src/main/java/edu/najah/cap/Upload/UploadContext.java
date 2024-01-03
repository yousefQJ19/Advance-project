package edu.najah.cap.Upload;

import jakarta.mail.MessagingException;

import java.io.IOException;

public class UploadContext {

    private IUploadData context;

    public void setContext(IUploadData Upload){
        this.context=Upload;
    }
    public void getContext(String email) throws MessagingException, IOException {
        context.Send(email);
    }
}
