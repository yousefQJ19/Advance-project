package edu.najah.cap.data.exporter;

import edu.najah.cap.iam.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UserProfileExporter {
    private final Logger logger = LoggerFactory.getLogger(UserProfileExporter.class);

    public void exportUserProfile(UserProfile user, ZipOutputStream zipOut) {
        try {
            String userProfileFileName = "user_profile.txt";
            String userProfileData = "First Name: " + user.getFirstName() + "\n" +
                    "Last Name: " + user.getLastName() + "\n" +
                    "Phone Number: " + user.getPhoneNumber() + "\n" +
                    "Email: " + user.getEmail() + "\n" +
                    "Username: " + user.getUserName() + "\n" +
                    "Role: " + user.getRole() + "\n" +
                    "Department: " + user.getDepartment() + "\n" +
                    "Organization: " + user.getOrganization() + "\n" +
                    "Country: " + user.getCountry() + "\n" +
                    "City: " + user.getCity() + "\n" +
                    "Street: " + user.getStreet() + "\n" +
                    "Postal Code: " + user.getPostalCode() + "\n" +
                    "Building: " + user.getBuilding() + "\n" +
                    "User Type: " + user.getUserType();

            ZipEntry userProfileEntry = new ZipEntry(userProfileFileName);
            zipOut.putNextEntry(userProfileEntry);
            zipOut.write(userProfileData.getBytes());
            zipOut.closeEntry();

            logger.info("User profile exported successfully for username: {}", user.getUserName());
        } catch (IOException e) {
            logger.error("Error during user profile export for username: {}", user.getUserName(), e);
        }
    }
}
