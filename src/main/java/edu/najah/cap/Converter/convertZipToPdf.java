package edu.najah.cap.Converter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;

public class convertZipToPdf implements IConvert{
    private static final Logger logger = LoggerFactory.getLogger(convertZipToPdf.class);
    @Override
    public void Convert(String pdfDirectory, String ZipDirectory) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(ZipDirectory));
             FileOutputStream fos = new FileOutputStream(pdfDirectory)) {

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

            logger.info("PDF conversion completed successfully. Input file: {}, Output file: {}");
        } catch (FileNotFoundException e) {
            logger.error("Input file not found: {}");
        } catch (IOException | DocumentException e) {
            logger.error("Error during PDF conversion. Input file: {}, Output file: {}");
        }
    }
}
