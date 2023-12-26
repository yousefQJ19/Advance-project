package edu.najah.cap.data.handler;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.data.PdfConverter;
import edu.najah.cap.data.ZipUtils;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.posts.IPostService;

import java.io.File;
import java.io.IOException;

public class ConvertHandler implements IDataHandler {

    private final IUserService userService;
    private final IPostService postService;
    private final IPayment paymentService;
    private final IUserActivityService userActivityService;

    public ConvertHandler(IUserService userService, IPostService postService, IPayment paymentService, IUserActivityService userActivityService) {
        this.userService = userService;
        this.postService = postService;
        this.paymentService = paymentService;
        this.userActivityService = userActivityService;
    }

    @Override
    public void exportUserData(String userId, String storagePath) {
        // ConvertHandler doesn't handle exporting, implement export logic separately
    }

    @Override
    public void deleteUserData(String userId, boolean hardDelete) {
        // ConvertHandler doesn't handle deletion, implement deletion logic separately
    }

    @Override
    public void convertToPdf(String inputFilePath, String outputFilePath) throws IOException {
        // Existing conversion logic...
        File file = new File(inputFilePath);

        if (file.exists()) {
            // Check if the file is a zip file
            if (file.getName().endsWith(".zip")) {
                try {
                    // Create a new directory to store the converted PDF files
                    String outputDirectory = file.getParent() + File.separator + "pdf_files";
                    File outputDir = new File(outputDirectory);
                    outputDir.mkdir();

                    // Extract the contents of the zip file
                    ZipUtils.extractZipFile(file.getAbsolutePath(), outputDirectory);

                    // Convert each extracted file to PDF
                    File[] extractedFiles = outputDir.listFiles();
                    if (extractedFiles != null) {
                        for (File extractedFile : extractedFiles) {
                            if (!extractedFile.isDirectory()) {
                                String pdfFilePath = extractedFile.getAbsolutePath() + ".pdf";
                                PdfConverter.convertToPdf(extractedFile.getAbsolutePath(), pdfFilePath);
                            }
                        }
                        System.out.println("Conversion to PDF completed successfully.");
                    } else {
                        System.out.println("No files found in the zip archive.");
                    }
                } catch (IOException e) {
                    System.out.println("Error converting files to PDF: " + e.getMessage());
                }
            } else {
                System.out.println("The provided file is not a zip archive.");
            }
        } else {
            System.out.println("The provided file does not exist.");
        }
    }
}
