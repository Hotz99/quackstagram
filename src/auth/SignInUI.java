package auth;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.xml.crypto.Data;

import user.InstagramProfileUI;
import user.User;
import utils.AppPaths;
import utils.UiUtils;

public class SignInUI extends JFrame {


  private static final int WIDTH = 300;
  private static final int HEIGHT = 500;
  private static final int HEIGHT_HEADERPANEL = 40; 
  private static final Color COLOR_WHITE = Color.WHITE;
  private static final Color BUTTON_BACKGROUND_COLOR = new Color(255, 90, 95);
  private static final Color BUTTON_TEXT_COLOR = Color.BLACK;
  private static final String FONT_NAME = "Arial";
  private static final String LABEL = "Quackstagram 🐥";
  private static final String SIGNIN_LABEL = "Sign-In";
  

  private JTextField txtUsername;
  private JTextField txtPassword;
  private JButton button, btnRegisterNow;
  private JLabel lblPhoto;
  private User newUser;

  public SignInUI() {
    setTitle("Quackstagram - Register");
    setSize(WIDTH, HEIGHT);
    setMinimumSize(new Dimension(WIDTH, HEIGHT));
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));
    initializeUI();
  }

  private void initializeUI() {
    JPanel headerPanel = UiUtils.createHeaderPanel(LABEL, WIDTH, HEIGHT_HEADERPANEL);
    JPanel photoPanel = UiUtils.getPhotoPanel(lblPhoto);
    JPanel fieldsPanel = UiUtils.getFieldsPanel(photoPanel, false);
    JPanel registerPanel = UiUtils.getRegisterPanel(
      SIGNIN_LABEL, 
      BUTTON_BACKGROUND_COLOR, 
      BUTTON_TEXT_COLOR, 
      FONT_NAME, 
      14, 
      this::onSignInClicked 
  );

    addPanel(headerPanel, fieldsPanel, registerPanel);
    navBtnSignUp();
    JPanel buttonPanel = getButtonPanel();
    add(buttonPanel, BorderLayout.SOUTH);
  }


  private void addPanel(JPanel headerPanel, JPanel fieldsPanel, JPanel registerPanel) {
    add(headerPanel, BorderLayout.NORTH);
    add(fieldsPanel, BorderLayout.CENTER);
    add(registerPanel, BorderLayout.SOUTH);
  }


  private JPanel getButtonPanel() {
    JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    buttonPanel.setBackground(COLOR_WHITE);
    buttonPanel.add(button);
    buttonPanel.add(btnRegisterNow);
    return buttonPanel;
  }
  

  private void navBtnSignUp() {
    btnRegisterNow = new JButton("No Account? Register Now");
    btnRegisterNow.addActionListener(this::onRegisterNowClicked);
    btnRegisterNow.setBackground(COLOR_WHITE);
    btnRegisterNow.setForeground(BUTTON_TEXT_COLOR);
    btnRegisterNow.setFocusPainted(false);
    btnRegisterNow.setBorderPainted(false);
  }


  private void onSignInClicked(ActionEvent event) {
    String enteredUsername = txtUsername.getText();
    String enteredPassword = txtPassword.getText();
    System.out.println(enteredUsername + " <-> " + enteredPassword);
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
