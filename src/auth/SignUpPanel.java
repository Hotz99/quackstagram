package auth;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import app.App;
import utils.BasePanel;

public class SignUpPanel extends BasePanel {
  public SignUpPanel() {
    JPanel headerPanel = createHeaderPanel(LABEL);
    JPanel photoPanel = getPhotoPanel(lblPhoto);
    JPanel fieldsPanel = getFieldsPanel();
    getUsername();
    getPassword();
    getBio();
    addStruct(photoPanel, fieldsPanel, true);
    btnUploadImage();
    photoUploadPanel(fieldsPanel);
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

  private void btnUploadImage() {
    btnUploadPhoto = new JButton("Upload Photo");
    btnUploadPhoto.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        handleProfilePictureUpload();
      }
    });
  }

  private void onRegisterClicked(ActionEvent event) {
    String username = getTxtUsername().getText();
    String password = getTxtPassword().getText();
    String bio = getTxtBio().getText();

    if (App.userManager.doesUsernameExist(username)) {
      JOptionPane.showMessageDialog(
          this,
          "Username already exists. Please choose a different username.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    } else {
      App.userManager.saveCredentials(username, password, bio);
      handleProfilePictureUpload();

      App.showPanel("SignIn");
    }
  }

  private void handleProfilePictureUpload() {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Image files",
        ImageIO.getReaderFileSuffixes());
    fileChooser.setFileFilter(filter);
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      App.userManager.saveProfilePicture(selectedFile, txtUsername.getText());
    }
  }
}
