package auth;

import app.App;
import database.users.UserRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import utils.BasePanel;
import utils.HeaderFactory;

public class SignUpPanel extends BasePanel {
  private final UserRepository userRepo = UserRepository.getInstance();

  public SignUpPanel() {
    super(true, true, true);
    JPanel headerPanel = HeaderFactory.createHeader(LABEL);
    JPanel photoPanel = getPhotoPanel(lblPhoto);
    JPanel fieldsPanel = getFieldsPanel();
    setupUsernameInput();
    setupPasswordInput();
    getBio();
    addStruct(photoPanel, fieldsPanel, true);
    setupBtnRegister(REGISTER_LABEL);
    JPanel registerPanel = registerPanel();
    addComponents(headerPanel, fieldsPanel, registerPanel);
    signInButton(registerPanel);
  }

  private void signInButton(JPanel registerPanel) {
    button = new JButton("Already have an account? Sign In");
    button.addActionListener(e -> App.showPanel("SignIn"));
    registerPanel.add(button, BorderLayout.SOUTH);
  }

  private JPanel registerPanel() {
    JPanel registerPanel = new JPanel(new BorderLayout());
    registerPanel.setBackground(Color.WHITE);
    registerPanel.add(btnRegister, BorderLayout.CENTER);
    return registerPanel;
  }

  private void setupBtnRegister(String label) {
    btnRegister = new JButton(label);
    btnRegister.addActionListener(this::onRegisterClicked);
    btnRegister.setBackground(new Color(255, 90, 95));
    btnRegister.setForeground(Color.BLACK);
    btnRegister.setFocusPainted(false);
    btnRegister.setBorderPainted(false);
    btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
  }

  private void onRegisterClicked(ActionEvent event) {
    String username = getUsernameInputField().getText();
    String password = getPasswordInputField().getText();
    String bio = getTxtBio().getText();

    if (userRepo.getByUsername(username) != null) {
      JOptionPane.showMessageDialog(
          this,
          "Username already exists.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    } else {

      UserManager.getInstance().setCurrentUser(userRepo.create(username, password, bio));
      handleProfilePictureUpload();
      App.showPanel("SignIn");
    }
  }
}
