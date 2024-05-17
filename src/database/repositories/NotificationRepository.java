package database.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseHandler;
import database.models.Notification;

public class NotificationRepository {
    public static void main(String[] args) {
        NotificationRepository notificationRepo = NotificationRepository.getInstance();

        System.out.println("All notifications for user 1:");

        for (Notification notification : notificationRepo.getAllByUserId(1)) {
            System.out.println(notification);
        }

    }

    private static NotificationRepository instance;

    private NotificationRepository() {
    }

    public static NotificationRepository getInstance() {
        if (instance == null) {
            instance = new NotificationRepository();
        }
        return instance;
    }

    public void saveNotification(Notification notification) {
        String query = "INSERT INTO notifications (notified_date, user_id, content) VALUES (?, ?, ?)";
        try (PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query)) {
            statement.setTimestamp(1, new Timestamp(notification.getNotifiedDate().getTime()));
            statement.setInt(2, notification.getUserId());
            statement.setString(3, notification.getContent());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("failed to save notification");
            e.printStackTrace();
        }
    }

    public List<Notification> getAllByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE user_id = ?";
        try (PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                notifications.add(new Notification(resultSet.getInt("notification_id"),
                        resultSet.getTimestamp("notified_date"), resultSet.getInt("user_id"),
                        resultSet.getString("content")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    public void deleteByNotificationId(int notificationId) {
        String query = "DELETE FROM notifications WHERE notification_id = ?";
        try (PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query)) {
            statement.setInt(1, notificationId);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("failed to delete notification with id: " + notificationId);
            e.printStackTrace();
        }
    }
}
