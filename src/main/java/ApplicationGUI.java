import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import edu.najah.cap.Converter.convertZipToPdf;
import edu.najah.cap.Delete.DeletContext;
import edu.najah.cap.Delete.HardDeleteProcessor;
import edu.najah.cap.Delete.SoftDeleteProcessor;
import edu.najah.cap.Upload.SendByEmail;
import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.activity.UserActivityService;
import edu.najah.cap.data.handler.ConvertHandler;
import edu.najah.cap.data.handler.ExportHandler;
import edu.najah.cap.data.handler.IDataHandler;
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.Instant;
import java.util.Scanner;

public class ApplicationGUI extends JFrame {
    private static final IUserActivityService userActivityService = new UserActivityService();
    private static final IPayment paymentService = new PaymentService();
    private static final IUserService userService = new UserService();
    private static final IPostService postService = new PostService();
    private static final Logger logger = LoggerFactory.getLogger(convertZipToPdf.class);
    private static String loginUserName;
    private JButton exportButton;
    private JButton convertButton;
    private JButton sendEmailButton;
    private JButton softDeleteButton;
    private JButton hardDeleteButton;

    public ApplicationGUI() {
        setTitle("Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        exportButton = new JButton("Export User Data");
        convertButton = new JButton("Convert to PDF");
        sendEmailButton = new JButton("Send Data by Email");
        softDeleteButton = new JButton("Soft Delete User Data");
        hardDeleteButton = new JButton("Hard Delete User Data");

        exportButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exportUserData();
            }

        });


        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertToPDF();
            }
        });


        sendEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendDataByEmail();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        softDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                softDeleteUserData();
            }
        });

        
        hardDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hardDeleteUserData();
            }
        });

        add(exportButton);
        add(convertButton);
        add(sendEmailButton);
        add(softDeleteButton);
        add(hardDeleteButton);

        pack();
        setLocationRelativeTo(null);
    }

    private void exportUserData() {
        generateRandomData();

        Instant start = Instant.now();
        System.out.println("Application started");

        System.out.println("Application Started: " + start);
        Scanner scanner = new Scanner(System.in);
        Util.setSkipValidation(false);
        System.out.println("Enter your username: ");
        System.out.println("Note: You can use any of the following usernames: user0, user1, user2, user3, .... user99");
        String userName = scanner.nextLine();
        setLoginUserName(userName);

        ExportHandler exportHandler = new ExportHandler(userService, postService, paymentService, userActivityService);

        String storagePath = "king/TextFiles";

        System.out.println("Exporting user data...");
        try {
            exportHandler.exportUserData(userName, storagePath);
            System.out.println("User data exported successfully\n");
            JOptionPane.showMessageDialog(this, "User data exported successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            System.out.println("Error exporting user data: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error exporting user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            logger.error("Error exporting user data", e);
        } catch (SystemBusyException | NotFoundException | BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    private void convertToPDF() {
        StringBuilder test = new StringBuilder(getLoginUserName());

        IDataHandler convertHandler = new ConvertHandler(userService, postService, paymentService, userActivityService);

        String inputFilePath = "king\\TextFiles\\" + test + "_data_.zip";
        String outputFilePath = "king\\pdf_files" + "\\" + test;
        System.out.println("\nConverting to PDF...");
        try {
            convertHandler.convertToPdf(inputFilePath, outputFilePath);
            System.out.println("Conversion to PDF completed successfully\n");
            JOptionPane.showMessageDialog(this, "Conversion to PDF completed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            logger.error("Error converting to PDF", e);
            System.out.println("Error converting to PDF: " + e.getMessage() + "\n");
            JOptionPane.showMessageDialog(this, "Error converting to PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendDataByEmail() throws IOException {
        System.out.println("Sending data by email...");
        SendByEmail s = new SendByEmail();
        s.Send("yousefnajeh03@gmail.com");
        System.out.println("Data sent by email successfully\n");
        JOptionPane.showMessageDialog(this, "Data sent by email successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void softDeleteUserData() {
        System.out.println("Soft deleting user data...");
        try {
            DeletContext softContext = new DeletContext(new SoftDeleteProcessor(userService, paymentService, postService, userActivityService));
            softContext.getContext(getLoginUserName());
            System.out.println("Soft deleting done successfully\n");
            JOptionPane.showMessageDialog(this, "Soft deleting done successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.out.println("Error in soft deleting user data: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error in soft deleting user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hardDeleteUserData() {
        System.out.println("Hard deleting user data...");
        try {
            DeletContext hardContext = new DeletContext(new HardDeleteProcessor(userService, paymentService, postService, userActivityService));
            hardContext.getContext(getLoginUserName());
            System.out.println("Hard deleting done successfully\n");
            JOptionPane.showMessageDialog(this, "Hard deleting done successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.out.println("Error in hard deleting user data: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error in hard deleting user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
                if (UserType.NEW_USER.equals(userService.getUser("user" + i).getUserType())) {
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

    public static void setLoginUserName(String loginUserName) {
        ApplicationGUI Application = null;
        ApplicationGUI.loginUserName = loginUserName;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ApplicationGUI().setVisible(true);
            }
        });
    }
}
