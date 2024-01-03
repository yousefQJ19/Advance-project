package edu.najah.cap.Delete;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivityService;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserService;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.payment.PaymentService;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.PostService;

public class DeleteFactory {

    private static final IUserActivityService userActivityService = new UserActivityService();
    private static final IPayment paymentService = new PaymentService();
    private static final IUserService userService = new UserService();
    private static final IPostService postService = new PostService();
    public static IDelete getDeletion(String type){
            if(type.equals("soft")){
                return new SoftDeleteProcessor(userService,  paymentService, postService, userActivityService);
            }
            if(type.equals("hard")){
                return  new HardDeleteProcessor(userService,  paymentService, postService, userActivityService);
            }
            return null;
    }
}
