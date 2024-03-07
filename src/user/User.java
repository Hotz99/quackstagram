package user;

import image.Picture;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a user in the system, maintaining user details,
 * followers, following, and posted pictures.
 */
public class User {

  private String username;
  private String bio;
  // Password storage should be hashed + salted in a real application
  private transient String password;
  private Set<User> followers;
  private Set<User> following;
  private List<Picture> pictures;

  /**
   * Constructs a new User with the specified username, bio, and password.
   * Initializes empty collections for followers, following, and pictures.
   *
   * @param username the username of the user
   * @param bio      the biography of the user
   * @param password the password of the user (to be hashed)
   */
  public User(String username, String bio, String password) {
    this.username = username;
    this.bio = bio;
    this.password = password; // Hash password here in a real application
    this.pictures = new ArrayList<>();
    this.followers = new HashSet<>();
    this.following = new HashSet<>();
  }

  /**
   * Constructs a new User object with the specified username.
   *
   * @param username the username of the user
   */
  public User(String username) {
    this.username = username;
  }

  /**
   * Adds a picture to the user's profile.
   *
   * @param picture the picture to add
   */
  public void addPicture(Picture picture) {
    pictures.add(picture);
  }

  // Getter and setter methods

  public String getUsername() {
    return username;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public int getPostsCount() {
    return pictures.size();
  }

  public int getFollowersCount() {
    return followers.size();
  }

  public int getFollowingCount() {
    return following.size();
  }

  public Set<User> getFollowers() {
    return new HashSet<>(followers);
  }

  public Set<User> getFollowing() {
    return new HashSet<>(following);
  }

  public List<Picture> getPictures() {
    return new ArrayList<>(pictures);
  }

  // Method to add a follower to this user
  protected void addFollower(User user) {
    followers.add(user);
  }

  // Method to add this user as a follower of another user
  protected void addFollowing(User user) {
    following.add(user);
  }

  @Override
  public String toString() {
    return username + ":" + bio + ":" + password; // Format as needed
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    User user = (User) o;
    return username.equals(user.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }
}
