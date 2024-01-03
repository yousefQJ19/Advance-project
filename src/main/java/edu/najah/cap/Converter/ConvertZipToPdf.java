package edu.najah.cap.Converter;

import edu.najah.cap.data.ZipUtils;

import java.io.File;
import java.io.IOException;

public class ConvertZipToPdf implements IConvert {
    private ConvertContext context=new ConvertContext();
    public ConvertZipToPdf() {
        this.context.setContext(ConvertFactory.getConverter("TextToPdf"));
    }
    @Override
    public  void Convert(String inputFilePath, String outputFilePath) throws IOException {
        // Existing conversion logic...
        File file = new File(inputFilePath);

        if (file.exists()) {
            // Check if the file is a zip file
            if (file.getName().endsWith(".zip")) {
                try {
                    // Create a new directory to store the converted PDF files
                    String outputDirectory = file.getParent() + File.separator +file.getName().substring(0,10)+ "_text_files";
                    File outputDir = new File(outputDirectory);

                    // Extract the contents of the zip file
                    ZipUtils.extractZipFile(file.getAbsolutePath(), outputDirectory);

                    // Convert each extracted file to PDF
                    char counter='a';
                    File[] extractedFiles = outputDir.listFiles();
                    if (extractedFiles != null) {
                        for (File extractedFile : extractedFiles) {
                            if (!extractedFile.isDirectory()) {
                                counter++;
                                context.getContext(extractedFile.getAbsolutePath(), outputFilePath+counter+".pdf");
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