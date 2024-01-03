package edu.najah.cap.Delete;

import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.iam.UserService;

public class AddUserConfirmation extends UserService{
    private UserService userservice;

    public AddUserConfirmation(UserService u){
        this.userservice=u;
    }
    public void addUser(UserProfile newUser){
        if(!HardDeleteProcessor.getDeletedUsersList().contains(newUser.getUserName())){
            super.addUser(newUser);
        }
        else{
            System.out.println("the user is already exited");
        }
    }
}
