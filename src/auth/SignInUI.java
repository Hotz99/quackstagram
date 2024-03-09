package auth;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.*;

import user.InstagramProfileUI;
import user.User;
import utils.AppPaths;
import utils.BaseFrame;

public class SignInUI extends BaseFrame {
  private userManager userManager;

  public SignInUI(auth.userManager userManager) {
    this.userManager = userManager;
    setTitle("Quackstagram - Register");
    setSize(APP_WIDTH, APP_HEIGHT);
    setMinimumSize(new Dimension(WIDTH, HEIGHT));
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));
    initializeUI();
  }

  private void initializeUI() {
    JPanel headerPanel = createHeaderPanel(LABEL);
    JPanel photoPanel = getPhotoPanel(lblPhoto);
    JPanel fieldsPanel = getFieldsPanel();
    getUsername();
    getPassword();
    addStruct(photoPanel, fieldsPanel, false);
    getButton(SIGNIN_LABEL);
    JPanel registerPanel = getRegisterPanel();
    addComponents(headerPanel, fieldsPanel, registerPanel);
    getNavSignUpBtn();
    getButtonPanel2();
  }

  private void getButtonPanel2() {
    JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); 
    buttonPanel.setBackground(Color.white);
    buttonPanel.add(button);
    buttonPanel.add(btnRegisterNow);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void getNavSignUpBtn() {
    btnRegisterNow = new JButton("No Account? Register Now");
    btnRegisterNow.addActionListener(this::onRegisterNowClicked);
    btnRegisterNow.setBackground(Color.WHITE); 
    btnRegisterNow.setForeground(Color.BLACK);
    btnRegisterNow.setFocusPainted(false);
    btnRegisterNow.setBorderPainted(false);
  }

 

  private void getButton(String label) {
    button = new JButton(label);
    button.addActionListener(this::onSignInClicked);
    button.setBackground(new Color(255, 90, 95)); 
    button.setForeground(Color.BLACK); 
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setFont(new Font("Arial", Font.BOLD, 14));
  }

  private JPanel getRegisterPanel() {
    JPanel registerPanel = new JPanel(new BorderLayout()); 
    registerPanel.setBackground(Color.WHITE);
    registerPanel.add(button, BorderLayout.CENTER);
    return registerPanel;
  }

  
  private void onSignInClicked(ActionEvent event) {
    String enteredUsername = getTxtUsername().getText();
    String enteredPassword = getTxtPassword().getText();
    System.out.println(enteredUsername + " <-> " + enteredPassword);
    
    createInstagramProfile(enteredUsername, enteredPassword);
  }



  // private void createInstagramProfile(String enteredUsername, String enteredPassword) {
  //   try {
  //     if (userManager.verifyCredentials(enteredUsername, enteredPassword)) {
  //       System.out.println("It worked" + newUser);
  //       openProfileUser(); 
  //     } else {
  //       System.out.println("It Didn't");
  //       JOptionPane.showMessageDialog(null, "Did not work, dumb fuck", "Alert", JOptionPane.WARNING_MESSAGE);
  //     }
  //   }
  //   catch (Exception e) {
  //     System.out.println("Error - You need to enter a username and password.");
  //     e.printStackTrace();
  //     JOptionPane.showMessageDialog(null, "You need to enter a username and password.", "Alert", JOptionPane.WARNING_MESSAGE);
  //   }
  // }

  private void createInstagramProfile(String enteredUsername, String enteredPassword) {
    try {
        User user = userManager.verifyCredentials(enteredUsername, enteredPassword);
        if (user != null) {
            newUser = user;
            System.out.println("It worked" + newUser);
            openProfileUser(); 
        } else {
            System.out.println("It Didn't");
            JOptionPane.showMessageDialog(null, "Did not work, dumb fuck", "Alert", JOptionPane.WARNING_MESSAGE);
        }
    }
    catch (Exception e) {
        System.out.println("Error - You need to enter a username and password.");
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "You need to enter a username and password.", "Alert", JOptionPane.WARNING_MESSAGE);
    }
}


  private void openProfileUser() {
    dispose();
    SwingUtilities.invokeLater(() -> {
      InstagramProfileUI profileUI = new InstagramProfileUI(newUser);
      profileUI.setVisible(true);
    });
  }

  private void onRegisterNowClicked(ActionEvent event) {
    dispose();

    SwingUtilities.invokeLater(() -> {
      SignUpUI signUpFrame = new SignUpUI(userManager);
      signUpFrame.setVisible(true);
    });
  }

  
  // private boolean verifyCredentials(String username, String password) {
  //   try (
  //       BufferedReader reader = new BufferedReader(
  //           new FileReader(
  //               AppPaths.CREDENTIALS))) {
  //     String line;
  //     while ((line = reader.readLine()) != null) {
  //       String[] credentials = line.split(":");
  //       if (credentials[0].equals(username) && credentials[1].equals(password)) {
  //         String bio = credentials[2];
  //         newUser = new User(username, bio, password);
  //         System.out.println("New User: " + newUser.getUsername());
  //         saveUserInformation(newUser);

  //         return true;
  //       }
  //     }
  //   } catch (IOException e) {
  //     e.printStackTrace();
  //   }
  //   return false;
  // }


  // private void saveUserInformation(User user) {
  //   System.out.println("Saving user information: " + user);

  //   try (
  //       BufferedWriter writer = new BufferedWriter(
  //           new FileWriter(
  //               AppPaths.USERS,
  //               false))) {
  //     writer.write(user.toString());
  //   } catch (IOException e) {
  //     e.printStackTrace();
  //   }
  // }
}
