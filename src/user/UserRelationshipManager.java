package user;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import utils.AppPaths;

/**
 * Manages relationships between users, including followers and following.
 * Loads and saves user relationships from a file.
 */
public class UserRelationshipManager {

  private static final String FOLLOWERS_FILE_PATH = AppPaths.FOLLOWERS;
  private Map<String, User> users = new HashMap<>();

  /**
   * Constructs a new UserRelationshipManager, loading user relationships from a file.
   *
   * @throws IOException if an I/O error occurs reading from the file
   */
  public UserRelationshipManager() throws IOException {
    loadUsers();
  }

  /**
   * Follows a user. If the follower or followed user does not exist,
   * or if the follower and followed user are the same, an exception is thrown.
   *
   * @param followerName the name of the follower
   * @param followedName the name of the followed user
   * @throws IOException if an I/O error occurs writing to the file
   */
  public void followUser(String followerName, String followedName)
    throws IOException {
    User follower = users.get(followerName);
    User followed = users.get(followedName);

    // Check if either user does not exist
    if (follower == null || followed == null) {
      throw new IllegalArgumentException(
        "User does not exist: " +
        (follower == null ? followerName : followedName)
      );
    }

    // Check if the follower and followed are the same user
    if (follower.equals(followed)) {
      throw new IllegalArgumentException("A user cannot follow themselves.");
    }

    // Add the follower and followed relationship, Set will handle duplicates
    follower.addFollowing(followed);
    followed.addFollower(follower);

    // Persist the changes
    saveUsers();
  }

  /**
   * Loads users and their relationships from a file.
   *
   * @throws IOException if an I/O error occurs reading from the file
   */
  private void loadUsers() throws IOException {
    try (
      BufferedReader reader = new BufferedReader(
        new FileReader(FOLLOWERS_FILE_PATH)
      )
    ) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(":");
        if (parts.length == 2) {
          User follower = users.computeIfAbsent(parts[0], User::new);
          User followed = users.computeIfAbsent(parts[1], User::new);
          follower.addFollowing(followed);
          followed.addFollower(follower);
        }
      }
    }
  }

  /**
   * Saves the current state of user relationships to a file.
   *
   * @throws IOException if an I/O error occurs writing to the file
   */
  private void saveUsers() throws IOException {
    try (
      BufferedWriter writer = new BufferedWriter(
        new FileWriter(FOLLOWERS_FILE_PATH)
      )
    ) {
      for (User user : users.values()) {
        for (User followed : user.getFollowing()) {
          writer.write(user.getUsername() + ":" + followed.getUsername());
          writer.newLine();
        }
      }
    }
  }
}
