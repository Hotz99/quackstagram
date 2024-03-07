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

public class SignInUI extends JFrame {

  private static final int WIDTH = 300;
  private static final int HEIGHT = 500;
  private static final Color HEADER_BACKGROUND_COLOR = new Color(51, 51, 51);
  private static final Color TEXT_COLOR_WHITE = Color.WHITE;
  private static final Color BUTTON_BACKGROUND_COLOR = new Color(255, 90, 95);
  private static final Color BUTTON_TEXT_COLOR = Color.BLACK;
  private static final Color TEXT_COLOR_GRAY = Color.GRAY;
  private static final String FONT_NAME = "Arial";
  private static final int FONT_SIZE = 16;

  private JTextField txtUsername;
  private JTextField txtPassword;
  private JButton btnSignIn, btnRegisterNow;
  private JLabel labelPhoto;
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
    JPanel headerPanel = getHeader();
    JPanel photoPanel = getPhotoPanel();
    JPanel fieldsPanel = getTextFieldsPanel();
    addStruct(photoPanel, fieldsPanel);
    JPanel registerPanel = getRegisterPanel();
    addPanel(headerPanel, fieldsPanel, registerPanel);
    navBtnSignUp();
    JPanel buttonPanel = getButtonPanel();
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private JPanel getHeader() {
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    headerPanel.setBackground(HEADER_BACKGROUND_COLOR);
    JLabel labelRegister = new JLabel("Quackstagram 🐥");
    labelRegister.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE));
    labelRegister.setForeground(TEXT_COLOR_WHITE);
    headerPanel.add(labelRegister);
    headerPanel.setPreferredSize(new Dimension(WIDTH, 40));
    return headerPanel;
  }

  private JPanel getPhotoPanel() {
    labelPhoto = new JLabel();
    labelPhoto.setPreferredSize(new Dimension(80, 80));
    labelPhoto.setHorizontalAlignment(JLabel.CENTER);
    labelPhoto.setVerticalAlignment(JLabel.CENTER);
    labelPhoto.setIcon(
        new ImageIcon(
            new ImageIcon(AppPaths.DACS_LOGO)
                .getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
    JPanel photoPanel = new JPanel();
    photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    photoPanel.add(labelPhoto);
    return photoPanel;
  }

  private JPanel getTextFieldsPanel() {
    JPanel fieldsPanel = new JPanel();
    fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
    fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

    txtUsername = new JTextField("Username");
    txtPassword = new JTextField("Password");
    txtUsername.setForeground(TEXT_COLOR_GRAY);
    txtPassword.setForeground(TEXT_COLOR_GRAY);
    return fieldsPanel;
  }

  private void addStruct(JPanel photoPanel, JPanel fieldsPanel) {
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(photoPanel);
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(txtUsername);
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(txtPassword);
    fieldsPanel.add(Box.createVerticalStrut(10));
  }

  private void addPanel(JPanel headerPanel, JPanel fieldsPanel, JPanel registerPanel) {
    add(headerPanel, BorderLayout.NORTH);
    add(fieldsPanel, BorderLayout.CENTER);
    add(registerPanel, BorderLayout.SOUTH);
  }

  private JPanel getButtonPanel() {
    JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    buttonPanel.setBackground(TEXT_COLOR_WHITE);
    buttonPanel.add(btnSignIn);
    buttonPanel.add(btnRegisterNow);
    return buttonPanel;
  }

  private void navBtnSignUp() {
    btnRegisterNow = new JButton("No Account? Register Now");
    btnRegisterNow.addActionListener(this::onRegisterNowClicked);
    btnRegisterNow.setBackground(TEXT_COLOR_WHITE);
    btnRegisterNow.setForeground(BUTTON_TEXT_COLOR);
    btnRegisterNow.setFocusPainted(false);
    btnRegisterNow.setBorderPainted(false);
  }

  private JPanel getRegisterPanel() {
    btnSignIn = new JButton("Sign-In");
    btnSignIn.addActionListener(this::onSignInClicked);
    btnSignIn.setBackground(BUTTON_BACKGROUND_COLOR);
    btnSignIn.setForeground(BUTTON_TEXT_COLOR);
    btnSignIn.setFocusPainted(false);
    btnSignIn.setBorderPainted(false);
    btnSignIn.setFont(new Font(FONT_NAME, Font.BOLD, 14));

    JPanel registerPanel = new JPanel();
    registerPanel.setBackground(TEXT_COLOR_WHITE);
    registerPanel.add(btnSignIn);

    return registerPanel;
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
