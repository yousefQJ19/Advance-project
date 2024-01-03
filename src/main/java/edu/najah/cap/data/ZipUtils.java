package edu.najah.cap.data;

import com.itextpdf.text.log.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
    private static Logger LoggerFactory;
    private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);

    public static void extractZipFile(String zipFilePath, String outputDirectory) throws IOException {
        logger.info("Extracting ZIP file: {} to directory: {}");

        byte[] buffer = new byte[1024];

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(outputDirectory + File.separator + fileName);
                logger.debug("Processing ZIP entry: {}");

                // Create directories if necessary
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                    logger.debug("Created directory: {}");

                } else {
                    // Create parent directories if necessary
                    new File(newFile.getParent()).mkdirs();

                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }

                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.closeEntry();
        }
    }
}