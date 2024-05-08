package post;

import java.util.Date;

public class Post {
  private int postId;
  private Date postedDate;
  private int userId;
  private String caption;
  private String imagePath;
  private int likesCount;
  private int commentsCount;

  public Post(int postId, Date postedDate, int userId, String caption, String imagePath, int likesCount,
      int commentsCount) {
    this.postId = postId;
    this.postedDate = postedDate;
    this.userId = userId;
    this.caption = caption;
    this.imagePath = imagePath;
    this.likesCount = likesCount;
    this.commentsCount = commentsCount;
  }

  @Override
  public String toString() {
    return "Post ID: " + postId +
        "\nPosted Date: " + postedDate +
        "\nUser ID: " + userId +
        "\nCaption: " + caption +
        "\nImage Path: " + imagePath +
        "\nLikes Count: " + likesCount +
        "\nComments Count: " + commentsCount;
  }

  public int getPostId() {
    return postId;
  }

  public void setPostId(int postId) {
    this.postId = postId;
  }

  public java.util.Date getPostedDate() {
    return postedDate;
  }

  public void setPostedDate(java.util.Date postedDate) {
    this.postedDate = postedDate;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public int getLikesCount() {
    return likesCount;
  }

  public void setLikesCount(int likesCount) {
    this.likesCount = likesCount;
  }

  public int getCommentsCount() {
    return commentsCount;
  }

  public void setCommentsCount(int commentsCount) {
    this.commentsCount = commentsCount;
  }
}