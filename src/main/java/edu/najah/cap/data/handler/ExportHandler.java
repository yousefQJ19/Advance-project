package edu.najah.cap.data.handler;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.data.exporter.UserActivitiesExporter;
import edu.najah.cap.data.exporter.UserPaymentExporter;
import edu.najah.cap.data.exporter.UserPostsExporter;
import edu.najah.cap.data.exporter.UserProfileExporter;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.iam.UserType;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.posts.IPostService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

public class ExportHandler  {

    private final IUserService userService;
    private final IPostService postService;
    private final IPayment paymentService;
    private final IUserActivityService userActivityService;

    public ExportHandler(IUserService userService, IPostService postService, IPayment paymentService, IUserActivityService userActivityService) {
        this.userService = userService;
        this.postService = postService;
        this.paymentService = paymentService;
        this.userActivityService = userActivityService;
    }

    public void exportUserData(String userId, String storagePath) throws IOException, SystemBusyException, NotFoundException, BadRequestException {
        UserProfile user = userService.getUser(userId);
        if (user != null) {
            try {
                String fileName = user.getUserName() + "_data_"  + ".zip";
                String filePath = storagePath + File.separator + fileName;

                FileOutputStream fos = new FileOutputStream(filePath);
                ZipOutputStream zipOut = new ZipOutputStream(fos);

                UserProfileExporter userProfileExporter = new UserProfileExporter();
                userProfileExporter.exportUserProfile(user, zipOut);

                UserPostsExporter userPostsExporter = new UserPostsExporter(postService);
                userPostsExporter.exportUserPosts(user.getUserName(), zipOut);

                if (user.getUserType() == UserType.PREMIUM_USER) {
                    UserPaymentExporter userPaymentExporter = new UserPaymentExporter(paymentService);
                    userPaymentExporter.exportUserPaymentInfo(user.getUserName(), zipOut);
                }
                if (user.getUserType()== UserType.PREMIUM_USER||user.getUserType()==UserType.REGULAR_USER){
                    UserActivitiesExporter userActivitiesExporter = new UserActivitiesExporter(userActivityService);
                    userActivitiesExporter.exportUserActivities(user.getUserName(), zipOut);
                }

                zipOut.close();
                fos.close();

                System.out.println("Data exported successfully. File path: " + filePath);
            } catch (IOException e) {
                System.out.println("Error exporting user data: " + e.getMessage());
            }
        } else {
            System.out.println("User not found.");
        }
    }

}
