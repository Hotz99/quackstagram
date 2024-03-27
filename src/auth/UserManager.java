package auth;

import app.App;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import user.User;
import utils.AppPathsSingleton;

public class UserManager {

  private User currentUser;
  private static UserManager instance = null;

  //Singleton pattern
  private final AppPathsSingleton appPathsSingleton = AppPathsSingleton.getInstance();
  private final String credentials = appPathsSingleton.CREDENTIALS;
  private final String profileImagesStorage =
    appPathsSingleton.PROFILE_IMAGES_STORAGE;
  private final String users = appPathsSingleton.USERS;

  public User getCurrentUser() {
    return this.currentUser;
  }

  public void setCurrentUser(User user) {
    this.currentUser = user;
  }

  public boolean doesUsernameExist(String username) {
    try (
      BufferedReader reader = new BufferedReader(new FileReader(credentials))
    ) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith(username + ":")) {
          return true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public void saveProfilePicture(File file, String username) {
    try {
      BufferedImage image = ImageIO.read(file);
      File outputFile = new File(profileImagesStorage + username + ".png");
      ImageIO.write(image, "png", outputFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveCredentials(String username, String password, String bio) {
    try (
      BufferedWriter writer = new BufferedWriter(
        new FileWriter(credentials, true)
      )
    ) {
      writer.write(username + ":" + password + ":" + bio);
      writer.newLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public User verifyCredentials(
    String enteredUsername,
    String enteredPassword
  ) {
    try (
      BufferedReader reader = new BufferedReader(new FileReader(credentials))
    ) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(":");
        String username = parts[0];
        String password = parts[1];
        if (
          username.equals(enteredUsername) && password.equals(enteredPassword)
        ) {
          currentUser = new User(username, password);
          App.createPanels();
          System.out.println("User verified: " + currentUser);
          App.showPanel("Home");
          return currentUser;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void saveUserInformation(User NewUser) {
    System.out.println("Saving user information: " + NewUser);

    try (
      BufferedWriter writer = new BufferedWriter(new FileWriter(users, false))
    ) {
      writer.write(NewUser.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Private constructor
  private UserManager() {}

  // Public static method to get the single instance of UserManager
  public static UserManager getInstance() {
    if (instance == null) {
      instance = new UserManager();
    }
    return instance;
  }
}
