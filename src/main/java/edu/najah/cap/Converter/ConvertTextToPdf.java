package edu.najah.cap.Converter;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import java.io.IOException;

import static java.awt.SystemColor.text;

public class ConvertTextToPdf implements IConvert{
    @Override
    public void Convert(String text, String pdfFilePath) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700); // Adjust the position as needed
                contentStream.showText(text);
                contentStream.endText();
            }
            catch (IOException e){
                System.out.println("error adding the content to the document\n");
            }

            document.save(pdfFilePath);
        }
        catch (IOException e) {
            System.out.println("Error converting files to PDF: " + e.getMessage());
        }
    }
}
