        package edu.najah.cap.Delete;

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
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import java.util.List;

        public class SoftDeleteProcessor implements IDelete{
    private final IPayment paymentService;
    private final IPostService postService;
    private final   IUserService userService;
    private final IUserActivityService userActivityService;
    private final Logger logger;

    public SoftDeleteProcessor(IUserService userService, IPayment paymentService, IPostService postService, IUserActivityService userActivityService) {
        this.paymentService = paymentService;
        this.postService = postService;
        this.userActivityService = userActivityService;
        this.userService=userService;
        this.logger = LoggerFactory.getLogger(SoftDeleteProcessor.class);
    }
    @Override
    public void delete(String userId) throws SystemBusyException, BadRequestException, NotFoundException {
        UserProfile test= userService.getUser(userId);
        deletePosts(userId);
        if (test.getUserType().equals(UserType.PREMIUM_USER)||
                test.getUserType().equals(UserType.REGULAR_USER)){
            deleteUserActivity(userId);
        }
        if (test.getUserType().equals(UserType.PREMIUM_USER)){
            deletePaymentTransactions(userId);
        }
        logger.info("User data deleted for userId: {}", userId);
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
        logger.debug("Posts deleted for userId: {}", userId);
    }

    private void deleteUserActivity(String userId) throws SystemBusyException, BadRequestException, NotFoundException {
        List<UserActivity> activity=userActivityService.getUserActivity(userId);
        if(activity!=null){
            activity.clear();
        }
    }
}