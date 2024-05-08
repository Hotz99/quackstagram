// package user;

// import image.Picture;
// import java.io.BufferedReader;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Objects;
// import java.util.Set;
// import utils.AppPathsSingleton;

// /**
//  * Represents a user in the system, maintaining user details,
//  * followers, following, and posted pictures.
//  */
// public class User {

//   private String username;
//   private String bio;
//   // Password storage should be hashed + salted in a real application
//   private transient String password;
//   private Set<User> followers;
//   private Set<User> following;
//   private List<Picture> pictures;

//   private int postsCount;
//   private int followersCount;
//   private int followingCount;

//   private final AppPathsSingleton appPathsSingleton = AppPathsSingleton.getInstance();
//   private final String users = appPathsSingleton.USERS;
//   private final String imageDetails = appPathsSingleton.IMAGE_DETAILS;
//   private final String followingPath = appPathsSingleton.FOLLOWING;

//   /**
//    * Constructs a new User with the specified username, bio, and password.
//    * Initializes empty collections for followers, following, and pictures.
//    *
//    * @param username the username of the user
//    * @param bio      the biography of the user
//    * @param password the password of the user (to be hashed)
//    */
//   public User(String username, String bio, String password) {
//     this.username = username;
//     this.bio = bio;
//     this.password = password; // Hash password here in a real application
//     this.pictures = new ArrayList<>();
//     this.followers = new HashSet<>();
//     this.following = new HashSet<>();
//   }

//   public User(String username, String password) {
//     this.username = username;
//     this.password = password; // Hash password here in a real application
//     this.pictures = new ArrayList<>();
//     this.followers = new HashSet<>();
//     this.following = new HashSet<>();
//   }

//   /**
//    * Constructs a new User object with the specified username.
//    *
//    * @param username the username of the user
//    */
//   public User(String username) {
//     this.username = username;
//   }

//   /**
//    * Adds a picture to the user's profile.
//    *
//    * @param picture the picture to add
//    */
//   public void addPicture(Picture picture) {
//     pictures.add(picture);
//   }

//   // Getter and setter methods

//   public String getUsername() {
//     return username;
//   }

//   public String getBio() {
//     return bio;
//   }

//   public void setBio(String bio) {
//     this.bio = bio;
//   }

//   public int getPostsCount() {
//     return postsCount;
//   }

//   public void loadPostsCount() {
//     int postsCount = 0;

//     Path imageDetailsFilePath = Paths.get(imageDetails);
//     try (
//         BufferedReader imageDetailsReader = Files.newBufferedReader(
//             imageDetailsFilePath)) {
//       String line;
//       while ((line = imageDetailsReader.readLine()) != null) {
//         if (line.contains("Username: " + this.getUsername())) {
//           postsCount++;
//         }
//       }
//     } catch (IOException e) {
//       e.printStackTrace();
//     }

//     this.postsCount = postsCount;
//   }

//   public void loadFollowsCount() {
//     int followersCount = 0;
//     int followingCount = 0;

//     Path followingFilePath = Paths.get(followingPath);
//     try (
//         BufferedReader followingReader = Files.newBufferedReader(
//             followingFilePath)) {
//       String line;
//       while ((line = followingReader.readLine()) != null) {
//         String[] parts = line.split(":");
//         if (parts.length == 2) {
//           String username = parts[0].trim();
//           String[] followingUsers = parts[1].split(";");
//           if (username.equals(this.getUsername())) {
//             followingCount = followingUsers.length;
//           } else {
//             for (String followingUser : followingUsers) {
//               if (followingUser.trim().equals(this.getUsername())) {
//                 followersCount++;
//               }
//             }
//           }
//         }
//       }
//     } catch (IOException e) {
//       e.printStackTrace();
//     }

//     this.followersCount = followersCount;
//     this.followingCount = followingCount;
//   }

//   public void loadBio() {
//     String bio = "";

//     Path bioDetailsFilePath = Paths.get(users);
//     try (
//         BufferedReader bioDetailsReader = Files.newBufferedReader(
//             bioDetailsFilePath)) {
//       String line;
//       while ((line = bioDetailsReader.readLine()) != null) {
//         String[] parts = line.split(":");
//         if (parts[0].equals(this.getUsername()) && parts.length >= 2) {
//           bio = parts[1];
//           break; // Exit the loop once the matching bio is found
//         }
//       }
//     } catch (IOException e) {
//       e.printStackTrace();
//     }

//     System.out.println("Loaded bio for " + this.getUsername() + ": " + bio);
//     this.setBio(bio);
//   }

//   public int getFollowersCount() {
//     // return followers.size();
//     return followersCount;
//   }

//   public int getFollowingCount() {
//     // return following.size();
//     return followingCount;
//   }

//   public Set<User> getFollowers() {
//     return new HashSet<>(followers);
//   }

//   public Set<User> getFollowing() {
//     return new HashSet<>(following);
//   }

//   public List<Picture> getPictures() {
//     return new ArrayList<>(pictures);
//   }

//   // Method to add a follower to this user
//   protected void addFollower(User user) {
//     followers.add(user);
//   }

//   // Method to add this user as a follower of another user
//   protected void addFollowing(User user) {
//     following.add(user);
//   }

//   @Override
//   public String toString() {
//     return username + ":" + bio + ":" + password; // Format as needed
//   }

//   @Override
//   public boolean equals(Object o) {
//     if (this == o)
//       return true;
//     if (o == null || getClass() != o.getClass())
//       return false;
//     User user = (User) o;
//     return username.equals(user.username);
//   }

//   @Override
//   public int hashCode() {
//     return Objects.hash(username);
//   }
// }

package user;

import java.util.Date;

public class User {
  private int userId;
  private Date createdDate;
  private String username;
  private String password;
  private String profileImagePath;
  private String bio;
  private int postsCount;
  private int followersCount;
  private int followingCount;

  public User(int userId, Date createdDate, String username, String password,
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
