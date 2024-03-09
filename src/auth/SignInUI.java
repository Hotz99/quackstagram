package auth;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

import user.InstagramProfileUI;
import user.User;
import utils.AppPaths;
import utils.BaseFrame;

public class SignInUI extends BaseFrame {

  


  public SignInUI() {
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
    String enteredUsername = txtUsername.getText();
    String enteredPassword = txtPassword.getText();
    System.out.println(enteredUsername + " <-> " + enteredPassword);
    
    try {
      if (verifyCredentials(enteredUsername, enteredPassword)) {
        System.out.println("It worked" + newUser);
        dispose();
        SwingUtilities.invokeLater(() -> {
          InstagramProfileUI profileUI = new InstagramProfileUI(newUser);
          profileUI.setVisible(true);
        }); 
      } else {
        System.out.println("It Didn't");
      }
    }
    catch (Exception e) {
      System.out.println("Error - You need to enter a username and password.");
      e.printStackTrace();
    }
  }

  private void onRegisterNowClicked(ActionEvent event) {
    dispose();

    SwingUtilities.invokeLater(() -> {
      SignUpUI signUpFrame = new SignUpUI();
      signUpFrame.setVisible(true);
    });
  }

  
  private boolean verifyCredentials(String username, String password) {
    try (
        BufferedReader reader = new BufferedReader(
            new FileReader(
                AppPaths.CREDENTIALS))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] credentials = line.split(":");
        if (credentials[0].equals(username) && credentials[1].equals(password)) {
          String bio = credentials[2];
          newUser = new User(username, bio, password);
          System.out.println("New User: " + newUser.getUsername());
          saveUserInformation(newUser);

          return true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }


  private void saveUserInformation(User user) {
    System.out.println("Saving user information: " + user);

    try (
        BufferedWriter writer = new BufferedWriter(
            new FileWriter(
                AppPaths.USERS,
                false))) {
      writer.write(user.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
