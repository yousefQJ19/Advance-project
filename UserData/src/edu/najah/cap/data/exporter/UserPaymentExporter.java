package edu.najah.cap.data.exporter;

import edu.najah.cap.payment.IPayment;
import edu.najah.cap.payment.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UserPaymentExporter {
    private final IPayment paymentService;
    private final Logger logger;

    public UserPaymentExporter(IPayment paymentService) {
        this.paymentService = paymentService;
        this.logger = LoggerFactory.getLogger(UserPaymentExporter.class);
    }

    public void exportUserPaymentInfo(String userId, ZipOutputStream zipOut) {
        try {
            List<Transaction> transactions = paymentService.getTransactions(userId);
            if (transactions != null && !transactions.isEmpty()) {
                String paymentInfoFileName = "user_payment_info.txt";
                StringBuilder paymentInfoData = new StringBuilder();
                for (Transaction transaction : transactions) {
                    paymentInfoData.append("Amount: ").append(transaction.getAmount()).append("\n")
                            .append("Description: ").append(transaction.getDescription()).append("\n\n");
                }

                ZipEntry paymentInfoEntry = new ZipEntry(paymentInfoFileName);
                zipOut.putNextEntry(paymentInfoEntry);
                zipOut.write(paymentInfoData.toString().getBytes());
                zipOut.closeEntry();

                logger.info("User payment information exported successfully for userId: {}", userId);
            } else {
                logger.warn("No payment transactions found for userId: {}", userId);
            }
        } catch (IOException e) {
            logger.error("Error during user payment information export for userId: {}", userId, e);
        }
    }
}
