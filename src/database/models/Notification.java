package database.models;

import java.util.Date;

public class Notification {
    private int notificationId;
    private Date notifiedDate;
    private int userId;
    private String content;

    public Notification(int notificationId, Date notifiedDate, int userId, String content) {
        this.notificationId = notificationId;
        this.notifiedDate = notifiedDate;
        this.userId = userId;
        this.content = content;
    }

    public Notification(Date notifiedDate, int userId, String content) {
        this.notifiedDate = notifiedDate;
        this.userId = userId;
        this.content = content;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Date getNotifiedDate() {
        return notifiedDate;
    }

    public void setNotifiedDate(Date likedDate) {
        this.notifiedDate = likedDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return notifiedDate + " - " + content;
    }
}
