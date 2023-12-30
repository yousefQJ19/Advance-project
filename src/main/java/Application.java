import edu.najah.cap.Converter.ConvertContext;
import edu.najah.cap.Upload.*;
import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.activity.UserActivityService;
import edu.najah.cap.data.ConsoleOutputStream;
import edu.najah.cap.data.handler.ConvertHandler;
import edu.najah.cap.data.handler.DeleteHandler;
import edu.najah.cap.data.handler.ExportHandler;
import edu.najah.cap.data.handler.IDataHandler;
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
import edu.najah.cap.Converter.*;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Instant;

public class Application {

    private static final IUserActivityService userActivityService = new UserActivityService();
    private static final IPayment paymentService = new PaymentService();
    private static final IUserService userService = new UserService();
    private static final IPostService postService = new PostService();

    public static void main(String[] args) throws IOException {

        generateRandomData();
        Instant start = Instant.now();
        System.out.println("Application Started: " + start);
        JFrame frame = new JFrame("Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JTextArea consoleTextArea = new JTextArea();
        consoleTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(consoleTextArea);

        frame.add(scrollPane, BorderLayout.CENTER);

        PrintStream consolePrintStream = new PrintStream(new ConsoleOutputStream(consoleTextArea));
        System.setOut(consolePrintStream);
        System.setErr(consolePrintStream);

        frame.setVisible(true);

        IDataHandler exportHandler = new ExportHandler(userService, postService, paymentService, userActivityService);
        IDataHandler deleteHandler = new DeleteHandler(userService, postService, paymentService, userActivityService);
        IDataHandler convertHandler = new ConvertHandler(userService, postService, paymentService, userActivityService);

        String userId = "user10";
        String storagePath = "king/pdf_files/newdataa";

        // Export
        System.out.println("Exporting user data...");
        try {
            exportHandler.exportUserData(userId, storagePath);
        } catch (IOException e) {
            System.out.println("Error exporting user data: " + e.getMessage());
        }

        // Convert
        String inputFilePath = "king/pdf_files/newdataa/ZipFiles.zip";
        String outputFilePath = "king/pdf_files/newdataa.zip";
        System.out.println("Converting to PDF...");
        try {
            convertHandler.convertToPdf(inputFilePath, outputFilePath);
        } catch (IOException e) {
            System.out.println("Error converting to PDF: " + e.getMessage());
        }

        //convert pdf to zip
        StringBuilder inputFilePathPdf=
                new StringBuilder("king/pdf_files/newdataa/");
        StringBuilder outPutFilePathZip=
                new StringBuilder("king/pdf_files/ZipFiles/yousef.zip");
            try {
                ConvertContext context=new ConvertContext();
                context.setContext(new ConvertPDFtoZip());
                context.getContext(inputFilePathPdf.toString(),outPutFilePathZip.toString());
                System.out.println("Converted pdf to zip Succefully\n");
            }
            catch (Exception e){
                System.out.println("Error converting Pdf to Zip: " + e.getMessage());
            }
       // send a zip file by email as an attachment
            SendByEmail s= new SendByEmail();
            s.Send("yousefnajeh03@gmail.com");
        // Delete (soft)
        boolean hardDelete = false;
        System.out.println("Soft deleting user data...");
        deleteHandler.deleteUserData(userId, hardDelete);

        // Delete (hard)
        String userIdToDeleteHard = "user10";
        boolean hardDeleteHard = true;
        System.out.println("Hard deleting user data...");
        deleteHandler.deleteUserData(userIdToDeleteHard, hardDeleteHard);
        //Convert pdf to zip
      Instant end = Instant.now();
        System.out.println("Application Ended: " + end);

    }


    private static void generateRandomData() {
        for (int i = 0; i < 100; i++) {
            generateUser(i);
            generatePost(i);
            generatePayment(i);
            generateActivity(i);
        }
        System.out.println("Data Generation Completed");
    }

    private static void generateActivity(int i) {
        for (int j = 0; j < 100; j++) {
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
}
