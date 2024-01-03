package edu.najah.cap.Delete;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.iam.UserType;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.payment.Transaction;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.Post;

import java.util.List;

public class  HardDeleteProcessor implements IDelete {
    private final IUserService userService;
    private final IPayment paymentService;
    private final IPostService postService;
    private final IUserActivityService userActivityService;
    private final Logger logger;

    public HardDeleteProcessor(IUserService userService, IPayment paymentService, IPostService postService, IUserActivityService userActivityService) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.postService = postService;
        this.userActivityService = userActivityService;
        this.logger = LoggerFactory.getLogger(HardDeleteProcessor.class);
    }
    @Override
    public void delete(String userId) {
        new Thread(() -> {
            try {
                UserProfile test = userService.getUser(userId);
                deleteUser(userId);
                deletePosts(userId);
                if (test.getUserType().equals(UserType.PREMIUM_USER) ||
                        test.getUserType().equals(UserType.REGULAR_USER)) {
                    deleteUserActivity(userId);
                }
                if (test.getUserType().equals(UserType.PREMIUM_USER)) {
                    deletePaymentTransactions(userId);
                }
                logger.info("User data hard deleted for userId: {}");
            } catch (Exception e) {
                logger.error("Error in hard deleting user data for userId: {}");
            }
        }).start();
    }


    private void deleteUser(String userId) throws SystemBusyException, NotFoundException, BadRequestException {
        userService.deleteUser(userId);
        logger.info("User deleted for userId: {}");
    }

    private void deletePaymentTransactions(String userId) throws SystemBusyException, BadRequestException, NotFoundException {
        List<Transaction> transactions = paymentService.getTransactions(userId);
        if(transactions!=null){
            transactions.clear();
        }
    }


    private void deletePosts(String userId) throws SystemBusyException, BadRequestException, NotFoundException {
       List<Post> posts= postService.getPosts(userId);
        if (posts!=null){
            posts.clear();
        }
        logger.debug("Posts deleted for userId: {}");
    }

    private void deleteUserActivity(String userId) throws SystemBusyException, BadRequestException, NotFoundException {
      List<UserActivity> activity=userActivityService.getUserActivity(userId);
      if(activity!=null){
          activity.clear();
      }
    }
}