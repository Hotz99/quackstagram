package utils;

import java.io.BufferedReader;

import explore.ExploreUI;
import home.QuakstagramHomeUI;
import image.ImageUploadUI;

import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import javax.swing.*;

import app.App;
import notifications.NotificationsUI;
import user.InstagramProfileUI;
import user.User;
import utils.BaseFrame;

public class BaseFrame extends JFrame {

  protected static final int APP_WIDTH = App.WIDTH;
  protected static final int APP_HEIGHT = App.HEIGHT;
  private final int NAV_ICON_SIZE = 20; // Corrected size for bottom icons
  private static final int HEADER_HEIGHT = 40; // Corrected static size for bottom icons
  protected static final Color COLOR_WHITE = Color.WHITE;
  protected static final Color BUTTON_BACKGROUND_COLOR = new Color(255, 90, 95);
  protected static final Color BUTTON_TEXT_COLOR = Color.BLACK;
  protected static final String FONT_NAME = "Arial";
  protected static final String LABEL = "Quackstagram 🐥";
  protected static final String SIGNIN_LABEL = "Sign-In";
  protected static final String REGISTER_LABEL = "Register";

  protected JTextField txtUsername;
  protected JTextField txtPassword;
  protected JTextField txtBio;
  protected JButton btnRegister;
  protected JLabel lblPhoto;
  protected JButton btnUploadPhoto;
  protected JButton button, btnRegisterNow;
  protected User newUser;

  protected JTextField getTxtUsername() {
    return txtUsername;
  }

  protected void setTxtUsername(JTextField txtUsername) {
    this.txtUsername = txtUsername;
  }

  protected JTextField getTxtPassword() {
    return txtPassword;
  }

  protected void setTxtPassword(JTextField txtPassword) {
    this.txtPassword = txtPassword;
  }

  protected JTextField getTxtBio() {
    return txtBio;
  }

  protected void setTxtBio(JTextField txtBio) {
    this.txtBio = txtBio;
  }

  protected JButton getBtnRegister() {
    return btnRegister;
  }

  protected void setBtnRegister(JButton btnRegister) {
    this.btnRegister = btnRegister;
  }

  protected JLabel getLblPhoto() {
    return lblPhoto;
  }

  protected void setLblPhoto(JLabel lblPhoto) {
    this.lblPhoto = lblPhoto;
  }

  protected JButton getBtnUploadPhoto() {
    return btnUploadPhoto;
  }

  protected void setBtnUploadPhoto(JButton btnUploadPhoto) {
    this.btnUploadPhoto = btnUploadPhoto;
  }

  protected JButton getButton() {
    return button;
  }

  protected void setButton(JButton button) {
    this.button = button;
  }

  protected JButton getBtnRegisterNow() {
    return btnRegisterNow;
  }

  protected void setBtnRegisterNow(JButton btnRegisterNow) {
    this.btnRegisterNow = btnRegisterNow;
  }

  protected User getNewUser() {
    return newUser;
  }

  protected void setNewUser(User newUser) {
    this.newUser = newUser;
  }

  private final String[] iconPaths = {
      AppPaths.ICONS + "home.png",
      AppPaths.ICONS + "search.png",
      AppPaths.ICONS + "add.png",
      AppPaths.ICONS + "heart.png",
      AppPaths.ICONS + "profile.png",
  };

  private final String[] buttonTypes = {
      "home",
      "explore",
      "add",
      "notification",
      "profile",
  };

  protected JPanel createHeaderPanel(String label) {
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
    JLabel lblRegister = new JLabel(label);
    lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
    lblRegister.setForeground(Color.WHITE); // Set the text color to white
    headerPanel.add(lblRegister);
    headerPanel.setPreferredSize(new Dimension(APP_WIDTH, HEADER_HEIGHT)); // Give the header a fixed height
    return headerPanel;
  }

  protected JPanel createNavigationPanel() {
    // Create and return the navigation panel
    // Navigation Bar
    JPanel navigationPanel = new JPanel();
    navigationPanel.setBackground(new Color(249, 249, 249));
    navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
    navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    for (int i = 0; i < iconPaths.length; i++) {
      navigationPanel.add(
          createIconButton(
              iconPaths[i],
              buttonTypes[i]));
      navigationPanel.add(Box.createHorizontalGlue());
    }

    return navigationPanel;
  }

  protected JButton createIconButton(String iconPath, String buttonType) {
    ImageIcon iconOriginal = new ImageIcon(iconPath);

    Image iconScaled = iconOriginal
        .getImage()
        .getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);

    JButton button = new JButton(new ImageIcon(iconScaled));
    button.setBorder(BorderFactory.createEmptyBorder());
    button.setContentAreaFilled(false);

    switch (buttonType) {
      case "home":
        button.addActionListener(e -> {
          this.dispose();
          openHomeUI();
        });
        break;
      case "explore":
        button.addActionListener(e -> {
          this.dispose();
          openExploreUI();
        });
        break;
      case "add":
        button.addActionListener(e -> {
          this.dispose();
          openImageUploadUI();
        });
        break;
      case "notification":
        button.addActionListener(e -> {
          this.dispose();
          openNotificationsUI();
        });
        break;
      case "profile":
        button.addActionListener(e -> {
          this.dispose();
          openProfileUI();
        });
        break;
    }

    return button;
  }

  // Open QuackstagramHomeUI frame
  private void openHomeUI() {
    QuakstagramHomeUI homeUI = new QuakstagramHomeUI();
    homeUI.setVisible(true);
  }

  // Open ExploreUI frame
  private void openExploreUI() {
    ExploreUI explore = new ExploreUI();
    explore.setVisible(true);
  }

  // Open ImageUploadUI frame
  private void openImageUploadUI() {
    ImageUploadUI imageUploadUI = new ImageUploadUI();
    imageUploadUI.setVisible(true);
  }

  // Open NotificationsUI frame
  private void openNotificationsUI() {
    NotificationsUI notificationsUI = new NotificationsUI();
    notificationsUI.setVisible(true);
  }

  // Open InstagramProfileUI frame
  private void openProfileUI() {
    String loggedInUsername = "";

    // Read the logged-in user's username from users.txt
    try (
        BufferedReader reader = Files.newBufferedReader(
            Paths.get(AppPaths.USERS))) {
      String line = reader.readLine();

      if (line != null) {
        loggedInUsername = line.split(":")[0].trim();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    InstagramProfileUI profileUI = new InstagramProfileUI(new User(loggedInUsername));
    profileUI.setVisible(true);
  }

  protected JPanel getPhotoPanel(JLabel lblPhoto) {
    lblPhoto = new JLabel();
    lblPhoto.setPreferredSize(new Dimension(80, 80));
    lblPhoto.setHorizontalAlignment(JLabel.CENTER);
    lblPhoto.setVerticalAlignment(JLabel.CENTER);
    lblPhoto.setIcon(
        new ImageIcon(
            new ImageIcon(AppPaths.DACS_LOGO)
                .getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
    JPanel photoPanel = new JPanel();
    photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    photoPanel.add(lblPhoto);
    return photoPanel;
  }

  protected void addStruct(JPanel photoPanel, JPanel fieldsPanel, boolean isSignUp) {
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(photoPanel);
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(txtUsername);
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(txtPassword);
    fieldsPanel.add(Box.createVerticalStrut(10));

    if (isSignUp) {
      fieldsPanel.add(txtBio);
      fieldsPanel.add(Box.createVerticalStrut(10));
    }
  }

  protected void getPassword() {
    txtPassword = new JTextField("Password");
    txtPassword.setForeground(Color.GRAY);
  }

  protected void getUsername() {
    txtUsername = new JTextField("Username");
    txtUsername.setForeground(Color.GRAY);
  }

  protected void getBio() {
    txtBio = new JTextField("Bio");
    txtBio.setForeground(Color.GRAY);
  }

  protected JPanel getFieldsPanel() {
    JPanel fieldsPanel = new JPanel();
    fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
    fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
    return fieldsPanel;
  }

  protected void addComponents(JPanel headerPanel, JPanel fieldsPanel, JPanel registerPanel) {
    add(headerPanel, BorderLayout.NORTH);
    add(fieldsPanel, BorderLayout.CENTER);
    add(registerPanel, BorderLayout.SOUTH);
  }

  protected void photoUploadPanel(JPanel fieldsPanel) {
    JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    photoUploadPanel.add(btnUploadPhoto);
    fieldsPanel.add(photoUploadPanel);
  }

}
