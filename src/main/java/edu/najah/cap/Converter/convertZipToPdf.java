package edu.najah.cap.Converter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class convertZipToPdf {
    private static final Logger logger = LoggerFactory.getLogger(convertZipToPdf.class);

    public  void convertToPdf(String inputFilePath, String outputFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             FileOutputStream fos = new FileOutputStream(outputFilePath)) {

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            document.open();
            document.add(new Paragraph(content.toString()));
            document.close();

            logger.info("PDF conversion completed successfully. Input file: {}, Output file: {}", inputFilePath, outputFilePath);
        } catch (FileNotFoundException e) {
            logger.error("Input file not found: {}", inputFilePath, e);
        } catch (IOException | DocumentException e) {
            logger.error("Error during PDF conversion. Input file: {}, Output file: {}", inputFilePath, outputFilePath, e);
        }
    }
}
