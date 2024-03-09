package auth;

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
import utils.AppPaths;

public class UserManager {

  private User currentUser;

  public boolean doesUsernameExist(String username) {
    try (
        BufferedReader reader = new BufferedReader(
            new FileReader(AppPaths.CREDENTIALS))) {
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
      File outputFile = new File(AppPaths.PROFILE_IMAGES_STORAGE + username + ".png");
      ImageIO.write(image, "png", outputFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveCredentials(String username, String password, String bio) {
    try (
        BufferedWriter writer = new BufferedWriter(
            new FileWriter(
                AppPaths.CREDENTIALS,
                true))) {
      writer.write(username + ":" + password + ":" + bio);
      writer.newLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public User verifyCredentials(String enteredUsername, String enteredPassword) {
    try (
        BufferedReader reader = new BufferedReader(
            new FileReader(AppPaths.CREDENTIALS))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(":");
        String username = parts[0];
        String password = parts[1];
        if (username.equals(enteredUsername) && password.equals(enteredPassword)) {
          // If the credentials are valid, return the corresponding User object
          return new User(username, password);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    // If the credentials are not valid, return null
    return null;
  }

  public void saveUserInformation(User NewUser) {
    System.out.println("Saving user information: " + NewUser);

    try (
        BufferedWriter writer = new BufferedWriter(
            new FileWriter(
                AppPaths.USERS,
                false))) {
      writer.write(NewUser.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
