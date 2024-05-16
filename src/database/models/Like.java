package database.models;

import java.util.Date;

public class Like {
    private int userId;
    private int postId;
    private Date likedDate;

    public Like(int userId, int postId, Date likedDate) {
        this.userId = userId;
        this.postId = postId;
        this.likedDate = likedDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Date getLikedDate() {
        return likedDate;
    }

    public void setLikedDate(Date likedDate) {
        this.likedDate = likedDate;
    }
}
