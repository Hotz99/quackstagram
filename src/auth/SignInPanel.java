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

import app.App;
import user.ProfilePanel;
import user.User;
import utils.AppPaths;
import utils.BasePanel;

public class SignInPanel extends BasePanel {
  public SignInPanel() {
    JPanel headerPanel = createHeaderPanel(SIGNIN_LABEL);
    JPanel photoPanel = getPhotoPanel(lblPhoto);
    JPanel fieldsPanel = getFieldsPanel();

    addStruct(photoPanel, fieldsPanel, false);

    getUsername();
    getPassword();
    setupButton(SIGNIN_LABEL);

    JPanel registerPanel = getRegisterPanel();
    addComponents(headerPanel, fieldsPanel, registerPanel);

    setupNavSignUpBtn();
    getButtonPanel2();
  }

  private void getButtonPanel2() {
    JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    buttonPanel.setBackground(Color.white);
    buttonPanel.add(button);
    buttonPanel.add(btnRegisterNow);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void setupNavSignUpBtn() {
    btnRegisterNow = new JButton("No Account? Register Now");
    btnRegisterNow.addActionListener(this::onRegisterNowClicked);
    btnRegisterNow.setBackground(Color.WHITE);
    btnRegisterNow.setForeground(Color.BLACK);
    btnRegisterNow.setFocusPainted(false);
    btnRegisterNow.setBorderPainted(false);
  }

  private void setupButton(String label) {
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

  private void createInstagramProfile(String enteredUsername, String enteredPassword) {
    try {
      User user = App.userManager.verifyCredentials(enteredUsername, enteredPassword);

      if (user != null) {
        newUser = user;
        System.out.println("It worked" + newUser);
        openProfileUser();
      } else {
        System.out.println("It Didn't");
        JOptionPane.showMessageDialog(null, "Did not work, dumb fuck", "Alert", JOptionPane.WARNING_MESSAGE);
      }
    } catch (Exception e) {
      System.out.println("Error - You need to enter a username and password.");
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "You need to enter a username and password.", "Alert",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  private void openProfileUser() {
    SwingUtilities.invokeLater(() -> {
      ProfilePanel profileUI = new ProfilePanel(newUser);
      profileUI.setVisible(true);
    });
  }

  private void onRegisterNowClicked(ActionEvent event) {
    SwingUtilities.invokeLater(() -> {
      SignUpPanel signUpFrame = new SignUpPanel();
      signUpFrame.setVisible(true);
    });
  }
}
