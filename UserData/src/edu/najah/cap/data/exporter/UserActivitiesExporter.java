package edu.najah.cap.data.exporter;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

                logger.info("User activities exported successfully for userId: {}", userId);
            } else {
                logger.warn("No user activities found for userId: {}", userId);
            }
        } catch (IOException e) {
            logger.error("Error during user activities export for userId: {}", userId, e);
        }
    }
}
