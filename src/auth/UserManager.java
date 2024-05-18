package auth;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import database.models.User;
import database.repositories.UserRepository;

public class UserManager {
  private final UserRepository userRepo = UserRepository.getInstance();

  private User currentUser;
  private static UserManager instance = null;

  private UserManager() {
  }

  public static UserManager getInstance() {
    if (instance == null) {
      instance = new UserManager();
    }
    return instance;
  }

  public User getCurrentUser() {
    return this.currentUser;
  }

  public void setCurrentUser(User user) {
    this.currentUser = user;
  }

  public void saveProfilePicture(File file) {
    String profileImagePath = "resources/images/profile/" + currentUser.getUsername() + ".jpg";

    try {
      Path destination = Paths.get(profileImagePath);
      Files.copy(file.toPath(), destination);
      currentUser.setProfileImagePath(currentUser.getUsername() + ".jpg");
      userRepo.update(currentUser);
    } catch (IOException e) {
      System.out.println("error saving profile picture: " + e.getMessage());
    }
  }

  public User verifyCredentials(
      String inputUsername,
      String inputPassword) {

    User result = userRepo.getByUsername(inputUsername);

    if (result == null) {
      return null;
    }

    return result.getPassword().equals(inputPassword) ? result : null;
  }
}
