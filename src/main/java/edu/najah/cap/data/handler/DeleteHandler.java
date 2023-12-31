package edu.najah.cap.data.handler;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.posts.IPostService;
public class DeleteHandler implements IDataHandler {

    private final IUserService userService;
    private final IPostService postService;
    private final IPayment paymentService;
    private final IUserActivityService userActivityService;

    public DeleteHandler(IUserService userService, IPostService postService, IPayment paymentService, IUserActivityService userActivityService) {
        this.userService = userService;
        this.postService = postService;
        this.paymentService = paymentService;
        this.userActivityService = userActivityService;
    }

    @Override
    public void exportUserData(String userId, String storagePath) {
    }

    @Override
    public void deleteUserData(String userId, boolean hardDelete) throws SystemBusyException, NotFoundException, BadRequestException {
        UserProfile user = userService.getUser(userId);
//        if (user != null) {
//            if (hardDelete) {
//                HardDeleteProcessor hardDeleteProcessor = new HardDeleteProcessor(userService, paymentService, postService, userActivityService);
//                hardDeleteProcessor.deleteUserData(userId);
//
//                System.out.println("User data deleted successfully (including account).");
//            } else {
//                SoftDeleteProcessor softDeleteProcessor = new SoftDeleteProcessor(paymentService, postService, userActivityService);
//                softDeleteProcessor.deleteUserData(userId);
//
//                System.out.println("User data deleted successfully (except basic information).");
//            }
//        } else {
//            System.out.println("User not found.");
//        }
    }

    @Override
    public void convertToPdf(String inputFilePath, String outputFilePath) {
    }
}
