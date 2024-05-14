package auth;

import app.App;
import explore.ExplorePanel;
import home.HomePanel;
import notifications.NotificationsPanel;
import post.PostUploadPanel;

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
import user.ProfilePanel;
import user.User;
import utils.BasePanel;
import utils.HeaderFactory;

public class SignInPanel extends BasePanel {

  public SignInPanel() {
    super(true, true, false);
    JPanel headerPanel = HeaderFactory.createHeader(SIGNIN_LABEL);
    JPanel photoPanel = getPhotoPanel(lblPhoto);
    JPanel fieldsPanel = getFieldsPanel();

    addStruct(photoPanel, fieldsPanel, false);

    setupUsernameInput();
    setupPasswordInput();
    setupButton(SIGNIN_LABEL);

    JPanel registerPanel = getRegisterPanel();
    addComponents(headerPanel, fieldsPanel, registerPanel);

    setupNavSignUpBtn();
    getButtonPanel();
  }

  private void getButtonPanel() {
    JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    buttonPanel.setBackground(Color.white);
    buttonPanel.add(button);
    buttonPanel.add(btnRegisterNow);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void setupNavSignUpBtn() {
    btnRegisterNow = new JButton("No Account? Register Now");
    btnRegisterNow.addActionListener(this::onSignUpClicked);
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
    try {
      User user = App.userManager.verifyCredentials(
          getUsernameInputField().getText(),
          getPasswordInputField().getText());

      if (user != null) {
        UserManager.getInstance().setCurrentUser(user);

        SwingUtilities.invokeLater(() -> {
          App.addRemainingPanels();
          App.showProfileByUsername(user.getUsername());
        });
      } else {
        JOptionPane.showMessageDialog(
            null,
            "Invalid username or password.",
            "FAILED TO SIGN IN",
            JOptionPane.WARNING_MESSAGE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void onSignUpClicked(ActionEvent event) {
    App.showPanel("SignUp");
  }
}
