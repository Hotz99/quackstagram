package user;

import image.Picture;
import utils.AppPaths;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

  private int postsCount;
  private int followersCount;
  private int followingCount;

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

  public User(String username, String password) {
    this.username = username;
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
    return postsCount;
  }

  public void loadPostsCount() {
    int postsCount = 0;

    Path imageDetailsFilePath = Paths.get(
        AppPaths.IMAGE_DETAILS);
    try (
        BufferedReader imageDetailsReader = Files.newBufferedReader(
            imageDetailsFilePath)) {
      String line;
      while ((line = imageDetailsReader.readLine()) != null) {
        if (line.contains("Username: " + this.getUsername())) {
          postsCount++;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.postsCount = postsCount;
  }

  public void loadFollowsCount() {
    int followersCount = 0;
    int followingCount = 0;

    Path followingFilePath = Paths.get(
        AppPaths.FOLLOWING);
    try (
        BufferedReader followingReader = Files.newBufferedReader(
            followingFilePath)) {
      String line;
      while ((line = followingReader.readLine()) != null) {
        String[] parts = line.split(":");
        if (parts.length == 2) {
          String username = parts[0].trim();
          String[] followingUsers = parts[1].split(";");
          if (username.equals(this.getUsername())) {
            followingCount = followingUsers.length;
          } else {
            for (String followingUser : followingUsers) {
              if (followingUser.trim().equals(this.getUsername())) {
                followersCount++;
              }
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.followersCount = followersCount;
    this.followingCount = followingCount;
  }

  public int getFollowersCount() {
    // return followers.size();
    return followersCount;
  }

  public int getFollowingCount() {
    // return following.size();
    return followingCount;
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
