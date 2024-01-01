package edu.najah.cap.data.exporter;

import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UserPostsExporter {
    private final IPostService postService;
    private final Logger logger;

    public UserPostsExporter(IPostService postService) {
        this.postService = postService;
        this.logger = LoggerFactory.getLogger(UserPostsExporter.class);
    }

    public void exportUserPosts(String userId, ZipOutputStream zipOut) {
        try {
            List<Post> posts = postService.getPosts(userId);
            if (posts != null && !posts.isEmpty()) {
                String postsFileName = "user_posts.txt";
                StringBuilder postsData = new StringBuilder();
                for (Post post : posts) {
                    postsData.append("Title: ").append(post.getTitle()).append("\n")
                            .append("Body: ").append(post.getBody()).append("\n")
                            .append("Author: ").append(post.getAuthor()).append("\n")
                            .append("Date: ").append(post.getDate()).append("\n\n");
                }

                ZipEntry postsEntry = new ZipEntry(postsFileName);
                zipOut.putNextEntry(postsEntry);
                zipOut.write(postsData.toString().getBytes());
                zipOut.closeEntry();

                logger.info("User posts exported successfully for userId: {}", userId);
            } else {
                logger.warn("No posts found for userId: {}", userId);
            }
        } catch (IOException e) {
            logger.error("Error during user posts export for userId: {}", userId, e);
        } catch (SystemBusyException | BadRequestException | NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
