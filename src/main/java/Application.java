import edu.najah.cap.Converter.ConvertContext;
import edu.najah.cap.Converter.ConvertFactory;
import edu.najah.cap.Delete.DeletContext;
import edu.najah.cap.Delete.DeleteFactory;
import edu.najah.cap.Upload.SendByEmail;
import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.activity.UserActivityService;
import edu.najah.cap.data.handler.ExportHandler;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.exceptions.Util;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.iam.UserService;
import edu.najah.cap.iam.UserType;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.payment.PaymentService;
import edu.najah.cap.payment.Transaction;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.Post;
import edu.najah.cap.posts.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.Scanner;

public class Application {

    private static final IUserActivityService userActivityService = new UserActivityService();
    private static final IPayment paymentService = new PaymentService();
    private static final IUserService userService = new UserService();
    private static final IPostService postService = new PostService();
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static String loginUserName;

    public static void main(String[] args) throws IOException, SystemBusyException, BadRequestException, NotFoundException, InterruptedException {
        generateRandomData();
        Instant start = Instant.now();
        System.out.println("Application Started: " + start);
        Scanner scanner = new Scanner(System.in);
        Util.setSkipValidation(false);
        System.out.println("Enter your username: ");
        System.out.println("Note: You can use any of the following usernames: user0, user1, user2, user3, .... user99");
        String userName = scanner.nextLine();
        setLoginUserName(userName);



        ExportHandler exportHandler = new ExportHandler(userService, postService, paymentService, userActivityService);
        ConvertContext convertContext = new ConvertContext();

        //String userId = "user10";
        String storagePath = "king/TextFiles";

         //Export
        System.out.println("Exporting user data...");
        try {
            exportHandler.exportUserData(userName, storagePath);
        } catch (IOException e) {
            System.out.println("Error exporting user data: " + e.getMessage());
        }
        catch (SystemBusyException ex){
            Thread.sleep(100);
            exportHandler.exportUserData(userName, storagePath);
        }


        StringBuilder test =new StringBuilder(userName);
        convertContext.setContext(ConvertFactory.getConverter("ZipToPdf"));
        // Convert
        String inputFilePath = "king\\TextFiles\\"+test+"_data_.zip";
        String outputFilePath = "king\\pdf_files"+"\\"+ test;
        System.out.println("\nConverting to PDF...");
        try {
            convertContext.getContext(inputFilePath, outputFilePath);
        } catch (IOException e) {
            System.out.println("Error converting to PDF: " + e.getMessage()+"\n");
        }


        //convert pdf to zip
        System.out.println("\nconverting pdf to zip ...");

        StringBuilder inputFilePathPdf=
                      new StringBuilder("king\\pdf_files");
        StringBuilder outPutFilePathZip=
                      new StringBuilder("king\\ZipFiles\\"+test+".zip");

        convertContext.setContext(ConvertFactory.getConverter("PdfToZip"));
            try {
                convertContext.getContext(inputFilePathPdf.toString(),outPutFilePathZip.toString());
                System.out.println("Converted pdf to zip Successfully\n");
            }  catch (Exception e){
                System.out.println("Error converting Pdf to Zip: " + e.getMessage());
            }


        // send a zip file by email as an attachment
        System.out.println("sending data by email...");
            SendByEmail s= new SendByEmail();
            s.Send("yousefnajeh03@gmail.com");
        System.out.println("sending data by email successfully");


        DeletContext deleteContext= new DeletContext();
        deleteContext.setContext(DeleteFactory.getDeletion("soft"));
        //Delete (soft)
        System.out.println("Soft deleting user data...");
        try {
            deleteContext.getContext(userName);
            System.out.println("soft deleting done successfully\n");
        }
        catch (Exception e) {
            System.out.println("Error error in soft deleting user data : " + e.getMessage());
        }

//          // Delete (hard)
        deleteContext.setContext(DeleteFactory.getDeletion("soft"));
        System.out.println("Hard deleting user data...");
        try {
            deleteContext.getContext(userName);
            System.out.println("had deleting the user successfully\n");
        }
        catch (Exception e) {
            System.out.println("Error error in hard deleting user data : " + e.getMessage());
        }

      Instant end = Instant.now();
        System.out.println("Application Ended: " + end);

    }


    private static void generateRandomData() {
        Util.setSkipValidation(true);
        for (int i = 0; i < 100; i++) {
            generateUser(i);
            generatePost(i);
            generatePayment(i);
            generateActivity(i);
        }
        System.out.println("Data Generation Completed");
        Util.setSkipValidation(false);
    }


    private static void generateActivity(int i) {
        for (int j = 0; j < 100; j++) {
            try {
                if(UserType.NEW_USER.equals(userService.getUser("user" + i).getUserType())) {
                    continue;
                }
            } catch (Exception e) {
                System.err.println("Error while generating activity for user" + i);
            }
            userActivityService.addUserActivity(new UserActivity("user" + i, "activity" + i + "." + j, Instant.now().toString()));
        }
    }

    private static void generatePayment(int i) {
        for (int j = 0; j < 100; j++) {
            try {
                if (userService.getUser("user" + i).getUserType() == UserType.PREMIUM_USER) {
                    paymentService.pay(new Transaction("user" + i, i * j, "description" + i + "." + j));
                }
            } catch (Exception e) {
                System.err.println("Error while generating post for user" + i);
            }
        }
    }

    private static void generatePost(int i) {
        for (int j = 0; j < 100; j++) {
            postService.addPost(new Post("title" + i + "." + j, "body" + i + "." + j, "user" + i, Instant.now().toString()));
        }
    }

    private static void generateUser(int i) {
        UserProfile user = new UserProfile();
        user.setUserName("user" + i);
        user.setFirstName("first" + i);
        user.setLastName("last" + i);
        user.setPhoneNumber("phone" + i);
        user.setEmail("email" + i);
        user.setPassword("pass" + i);
        user.setRole("role" + i);
        user.setDepartment("department" + i);
        user.setOrganization("organization" + i);
        user.setCountry("country" + i);
        user.setCity("city" + i);
        user.setStreet("street" + i);
        user.setPostalCode("postal" + i);
        user.setBuilding("building" + i);
        user.setUserType(getRandomUserType(i));
        userService.addUser(user);
    }

    private static UserType getRandomUserType(int i) {
        if (i > 0 && i < 3) {
            return UserType.NEW_USER;
        } else if (i > 3 && i < 7) {
            return UserType.REGULAR_USER;
        } else {
            return UserType.PREMIUM_USER;
        }
    }

    public static String getLoginUserName() {
        return loginUserName;
    }

    private static void setLoginUserName(String loginUserName) {
        Application.loginUserName = loginUserName;
    }
}

