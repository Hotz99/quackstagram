package notifications;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.*;
import utils.BasePanel;
import utils.AppPaths;

public class NotificationsPanel extends BasePanel {
  public NotificationsPanel() {
    JPanel headerPanel = createHeaderPanel(" Notifications 🐥");

    JScrollPane scrollPane = new JScrollPane(createContentPanel());
    scrollPane.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    // Add panels to frame
    add(headerPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
  }

  private JPanel createContentPanel() {
    // Content Panel for notifications
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    // Read the current username from users.txt
    String currentUsername = "";

    try (
        BufferedReader reader = Files.newBufferedReader(
            Paths.get(AppPaths.USERS))) {
      String line = reader.readLine();
      if (line != null) {
        currentUsername = line.split(":")[0].trim();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    try (
        BufferedReader reader = Files.newBufferedReader(
            Paths.get(
                AppPaths.NOTIFICATIONS))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(";");
        if (!parts[0].trim().equals(currentUsername))
          continue;

        // Format the notification message
        String userWhoLiked = parts[1].trim();
        String timestamp = parts[3].trim();
        String notificationMessage = userWhoLiked +
            " liked your picture - " +
            getElapsedTime(timestamp) +
            " ago";

        // Add the notification to the panel
        JPanel notificationPanel = new JPanel(new BorderLayout());
        notificationPanel.setBorder(
            BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel notificationLabel = new JLabel(notificationMessage);
        notificationPanel.add(notificationLabel, BorderLayout.CENTER);

        contentPanel.add(notificationPanel);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return contentPanel;
  }

  private String getElapsedTime(String timestamp) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm:ss");
    LocalDateTime timeOfNotification = LocalDateTime.parse(
        timestamp,
        formatter);
    LocalDateTime currentTime = LocalDateTime.now();

    long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
    long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

    StringBuilder timeElapsed = new StringBuilder();
    if (daysBetween > 0) {
      timeElapsed
          .append(daysBetween)
          .append(" day")
          .append(daysBetween > 1 ? "s" : "");
    }
    if (minutesBetween > 0) {
      if (daysBetween > 0) {
        timeElapsed.append(" and ");
      }
      timeElapsed
          .append(minutesBetween)
          .append(" minute")
          .append(minutesBetween > 1 ? "s" : "");
    }
    return timeElapsed.toString();
  }
}