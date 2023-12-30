package edu.najah.cap.Converter;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertPDFtoZip implements IConvert{
    private static final Logger logger = LoggerFactory.getLogger(convertZipToPdf.class);
    @Override
    public void Convert(String pdfDirectory, String ZipDirectory) throws IOException {

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(ZipDirectory))) {
            File pdfFolder = new File(pdfDirectory);
            File[] pdfFiles = pdfFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

            if (pdfFiles != null) {
                for (File pdfFile : pdfFiles) {
                    addPdfToZip(pdfFile, zipOutputStream);
                }
            }
            logger.info("PDF conversion completed successfully. Input file: {}, Output file: {}", pdfDirectory, ZipDirectory);
        }
        catch (FileNotFoundException e){
            logger.error("Input file not found: {}", pdfDirectory, e);
        }
        catch (IOException e){
            logger.error("Error during PDF conversion. Input file: {}, Output file: {}", pdfDirectory, ZipDirectory, e);
        }
    }



        private static void addPdfToZip(File pdfFile, ZipOutputStream zipOutputStream) throws IOException {
            String pdfFileName = pdfFile.getName();
            String entryName = pdfFileName;

            zipOutputStream.putNextEntry(new ZipEntry(entryName));

            try (FileInputStream fis = new FileInputStream(pdfFile);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, bytesRead);
                }

                zipOutputStream.closeEntry();
            }
        }
}


