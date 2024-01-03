package edu.najah.cap.Converter;


import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ConvertPDFtoZip implements IConvert{
    private static final Logger logger = LoggerFactory.getLogger(ConvertPDFtoZip.class);
    @Override
    public void Convert(String pdfDirectory, String zipDirectory) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipDirectory))) {
            File pdfFolder = new File(pdfDirectory);
            File[] pdfFiles = pdfFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

            if (pdfFiles != null) {
                for (File pdfFile : pdfFiles) {
                    addPdfToZip(pdfFile, zipOutputStream);
                    Path path = Paths.get(pdfFile.toURI());
                    Files.delete(path);
                }
            }
            System.out.println("PDF conversion completed successfully. Input folder: " + pdfDirectory + ", Output zip file: " + zipDirectory);
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found: " + pdfDirectory);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error during PDF to Zip conversion. Input folder: " + pdfDirectory + ", Output zip file: " + zipDirectory);
            e.printStackTrace();
        }
    }

    private void addPdfToZip(File pdfFile, ZipOutputStream zipOutputStream) throws IOException {
        try (FileInputStream pdfInputStream = new FileInputStream(pdfFile)) {
            ZipEntry zipEntry = new ZipEntry(pdfFile.getName());
            zipOutputStream.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = pdfInputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, bytesRead);
            }

            zipOutputStream.closeEntry();

        }
    }

}


