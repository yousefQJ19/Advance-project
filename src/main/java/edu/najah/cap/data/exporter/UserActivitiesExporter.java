package edu.najah.cap.data.exporter;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UserActivitiesExporter {
    private final IUserActivityService userActivityService;
    private final Logger logger;

    public UserActivitiesExporter(IUserActivityService userActivityService) {
        this.userActivityService = userActivityService;
        this.logger = LoggerFactory.getLogger(UserActivitiesExporter.class);
    }

    public void exportUserActivities(String userId, ZipOutputStream zipOut) {
        try {
            List<UserActivity> activities = userActivityService.getUserActivity(userId);
            if (activities != null && !activities.isEmpty()) {
                String activitiesFileName = "user_activities.txt";
                StringBuilder activitiesData = new StringBuilder();
                for (UserActivity activity : activities) {
                    activitiesData.append("Activity ID: ").append(activity.getId()).append("\n")
                            .append("Activity Type: ").append(activity.getActivityType()).append("\n")
                            .append("Activity Date: ").append(activity.getActivityDate()).append("\n\n");
                }

                ZipEntry activitiesEntry = new ZipEntry(activitiesFileName);
                zipOut.putNextEntry(activitiesEntry);
                zipOut.write(activitiesData.toString().getBytes());
                zipOut.closeEntry();

                logger.info("User activities exported successfully for userId: {}");
            } else {
                logger.warn("No user activities found for userId: {}");
            }
        } catch (IOException e) {
            logger.error("Error during user activities export for userId: {}");
        } catch (SystemBusyException | BadRequestException | NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
