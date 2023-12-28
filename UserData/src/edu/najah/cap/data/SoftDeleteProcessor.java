        package edu.najah.cap.data;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.posts.IPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoftDeleteProcessor {
    private final IPayment paymentService;
    private final IPostService postService;
    private final IUserActivityService userActivityService;
    private final Logger logger;

    public SoftDeleteProcessor(IPayment paymentService, IPostService postService, IUserActivityService userActivityService) {
        this.paymentService = paymentService;
        this.postService = postService;
        this.userActivityService = userActivityService;
        this.logger = LoggerFactory.getLogger(SoftDeleteProcessor.class);
    }

    public void deleteUserData(String userId) {
        deletePaymentTransactions(userId);
        deletePosts(userId);
        deleteUserActivity(userId);
        logger.info("User data deleted for userId: {}", userId);
    }

    private void deletePaymentTransactions(String userId) {
        paymentService.removeTransaction(userId, null);
        logger.debug("Payment transactions deleted for userId: {}", userId);
    }

    private void deletePosts(String userId) {
        postService.deletePost(userId, null);
        logger.debug("Posts deleted for userId: {}", userId);
    }

    private void deleteUserActivity(String userId) {
        userActivityService.removeUserActivity(userId, null);
        logger.debug("User activity deleted for userId: {}", userId);
    }
}