package edu.najah.cap.Converter;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class ConvertTextToPdf implements IConvert{
    private static final Logger logger = LoggerFactory.getLogger(ConvertTextToPdf.class);
    @Override
    public void Convert(String textFilePath, String pdfFilePath) throws IOException {

        try {
            BufferedReader br = new BufferedReader(new FileReader(textFilePath));

            Document document = new Document();
            FileOutputStream fos = new FileOutputStream(pdfFilePath);
            PdfWriter writer = PdfWriter.getInstance(document, fos);

            document.open();

            String line;
            while ((line = br.readLine()) != null) {
                document.add(new Paragraph(line));
            }

            document.close();

            System.out.println("Conversion of " + textFilePath + " to " + pdfFilePath + " completed successfully.");
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
