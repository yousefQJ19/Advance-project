import edu.najah.cap.Converter.ConvertContext;
import edu.najah.cap.Converter.ConvertFactory;
import edu.najah.cap.Delete.DeletContext;
import edu.najah.cap.Delete.DeleteFactory;
import edu.najah.cap.Upload.SendByEmail;
import edu.najah.cap.Upload.UploadContext;
import edu.najah.cap.Upload.UploadToDropBox;
import edu.najah.cap.Upload.uploadToGoogleDrive;
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
import jakarta.mail.MessagingException;
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

    public static void main(String[] args) throws IOException, BadRequestException, NotFoundException, InterruptedException, MessagingException, SystemBusyException {
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
        DeletContext deleteContext= new DeletContext();
        deleteContext.setContext(DeleteFactory.getDeletion("soft"));
        ConvertContext convertContext = new ConvertContext();
        String storagePath = "king/TextFiles";

        int option=9999;
        while (option!=0){
            System.out.println("you operations");
            System.out.println("1- collect data");
            System.out.println("2-Soft delete");
            System.out.println("3-hard delete");
            option= scanner.nextInt();
            if (option ==1){
                //data collected
                System.out.println("Exporting user data...");
                int maxtryes=0;
                while (maxtryes !=3){

                    try  {
                        exportHandler.exportUserData(userName, storagePath);
                        break;
                    } catch (IOException | SystemBusyException e) {
                        Thread.sleep(1000);
                        System.out.println("Error exporting user data: " + e.getMessage());
                        maxtryes++;
                    }

                }
                StringBuilder test =new StringBuilder(userName);
                convertContext.setContext(ConvertFactory.getConverter("ZipToPdf"));

                // Convert zip(text) to pdf
                String inputFilePath = "king\\TextFiles\\"+test+"_data_.zip";
                String outputFilePath = "king\\pdf_files"+"\\"+ test;
                System.out.println("\nConverting to PDF...");
                int maxTry=0;
                while (maxTry !=3){
                    try {
                        convertContext.getContext(inputFilePath, outputFilePath);
                        break;
                    } catch (IOException  e) {
                        Thread.sleep(1000);
                        System.out.println("Error converting to PDF: " + e.getMessage()+"\n");
                        maxTry++;
                    }
                }


                System.out.println("\nconverting pdf to zip ...");

                //convert pdf to zip

                StringBuilder inputFilePathPdf=
                        new StringBuilder("king\\pdf_files");
                StringBuilder outPutFilePathZip=
                        new StringBuilder("king\\ZipFiles\\"+test+".zip");

                convertContext.setContext(ConvertFactory.getConverter("PdfToZip"));
                try {
                    convertContext.getContext(inputFilePathPdf.toString(),outPutFilePathZip.toString());
                    System.out.println("Converted pdf to zip Successfully\n");
                }  catch (IOException  e){
                    System.out.println("Error converting Pdf to Zip: " + e.getMessage());
                    convertContext.getContext(inputFilePathPdf.toString(),outPutFilePathZip.toString());
                }
                int option2=9999;
                UploadContext uploadContext=new UploadContext();
                System.out.println("where do you want you data to be upload");
                System.out.println("1- send the files in email as attachment");
                System.out.println("2- upload the files on google drive and receive the link as email ");
                System.out.println("3- upload the files on dropbox and receive the link as email ");
                option2= scanner.nextInt();
                if(option2 == 1){
                    int sMaxTry=0;
                    // send a zip file by email as an attachment
                    while(sMaxTry!=3){
                        try {
                            uploadContext.setContext(new SendByEmail());
                            System.out.println("sending data by email...");
                            uploadContext.getContext(userService.getUser(userName).getEmail());
                            System.out.println("sending data by email successfully");
                            break;
                        }
                        catch (SystemBusyException e){
                            Thread.sleep(1000);
                            System.out.println("Error sending the massage : " + e.getMessage());
                            sMaxTry++;
                        }
                    }


                }
                if(option2 == 2){
                    uploadContext.setContext(new uploadToGoogleDrive());
                    uploadContext.getContext(userService.getUser(userName).getEmail());
                }
                if(option2 == 3){
                    uploadContext.setContext(new UploadToDropBox());
                    uploadContext.getContext(userService.getUser(userName).getEmail());
                }

            } else if (option==2) {
                //soft delete
                int sMaxTry=0;
                System.out.println("Soft deleting user data...");
                while (sMaxTry !=3){
                    try {
                        deleteContext.getContext(userName);
                        System.out.println("soft deleting done successfully\n");
                        break;
                    }
                    catch ( SystemBusyException e) {
                        Thread.sleep(1000);
                        System.out.println("Error error in soft deleting user data : " + e.getMessage());
                        sMaxTry++;
                    }
                }

            }
            else if(option==3){
                //hard delete
                int sMaxTry=0;
                deleteContext.setContext(DeleteFactory.getDeletion("soft"));
                System.out.println("Hard deleting user data...");
                while (sMaxTry !=3){
                    try {
                        deleteContext.getContext(userName);
                        System.out.println("had deleting the user successfully\n");
                        break;
                    }
                    catch (Exception e) {
                        Thread.sleep(1000);
                        System.out.println("Error error in hard deleting user data : " + e.getMessage());
                        sMaxTry++;
                    }
                }


            }
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

