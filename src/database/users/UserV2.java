package database.users;

import java.util.Date;

public class UserV2 {
    private int userId;
    private Date createdDate;
    private String username;
    private String password;
    private String profileImagePath;
    private String bio;
    private int postsCount;
    private int followersCount;
    private int followingCount;

    public UserV2(int userId, Date createdDate, String username, String password,
            String profileImagePath, String bio, int postsCount, int followersCount, int followingCount) {
        this.userId = userId;
        this.createdDate = createdDate;
        this.username = username;
        this.password = password;
        this.profileImagePath = profileImagePath;
        this.bio = bio;
        this.postsCount = postsCount;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    @Override
    public String toString() {
        return "User ID: " + userId +
                "\nCreated Date: " + createdDate +
                "\nUsername: " + username +
                "\nPassword: " + password +
                "\nProfile Image Path: " + profileImagePath +
                "\nBio: " + bio +
                "\nPosts Count: " + postsCount +
                "\nFollowers Count: " + followersCount +
                "\nFollowing Count: " + followingCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImageUrl) {
        this.profileImagePath = profileImageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(int postsCount) {
        this.postsCount = postsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }
}
