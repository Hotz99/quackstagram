package utils;

import app.App;
import explore.ExplorePanel;
import home.HomePanel;
import image.ImageUploadPanel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import javax.swing.*;
import notifications.NotificationsPanel;
import user.ProfilePanel;
import user.User;
import utils.AppPathsSingleton;
import utils.BasePanel;

public class BasePanel extends JPanel {

  private final int NAV_ICON_SIZE = 20; // Corrected size for bottom icons
  private static final int HEADER_HEIGHT = 40;
  public static final Color COLOR_WHITE = Color.WHITE;
  public static final Color BUTTON_BACKGROUND_COLOR = new Color(255, 90, 95);
  public static final Color BUTTON_TEXT_COLOR = Color.BLACK;
  public static final String FONT_NAME = "Arial";
  public static final String LABEL = "Quackstagram 🐥";
  public static final String SIGNIN_LABEL = "Sign-In";
  public static final String REGISTER_LABEL = "Register";

  public JTextField txtUsername = new JTextField("Username");
  public JTextField txtPassword = new JTextField("Password");
  public JTextField txtBio = new JTextField("Bio");

  public JButton btnRegister = new JButton("Register");
  public JLabel lblPhoto = new JLabel();
  public JButton btnUploadPhoto = new JButton("Upload Photo");
  public JButton button, btnRegisterNow = new JButton(
    "No Account? Register Now"
  );
  public User newUser;

  //Singleton pattern
  private final AppPathsSingleton appPathsSingleton = AppPathsSingleton.getInstance();
  private final String users = appPathsSingleton.USERS;
  private final String dacsLogo = appPathsSingleton.DACS_LOGO;
  private final String[] iconPaths = appPathsSingleton.ICON_PATHS;
  private final String[] buttonTypes = appPathsSingleton.BUTTON_TYPES;

  public BasePanel(
    boolean includeUsername,
    boolean includePassword,
    boolean includeBio
  ) {
    if (includeUsername) {
      add(getTxtUsername());
      setupPlaceholder(txtUsername, "Username");
    }
    if (includePassword) {
      add(getTxtPassword());
      setupPlaceholder(txtPassword, "Password");
    }
    if (includeBio) {
      add(getTxtBio());
      setupPlaceholder(txtBio, "Bio");
    }
  }

  public JTextField getTxtUsername() {
    if (txtUsername.getText().equals("Username")) {
      setupPlaceholder(txtUsername, "Username");
    }
    return txtUsername;
  }

  public JTextField getTxtPassword() {
    if (txtPassword.getText().equals("Password")) {
      setupPlaceholder(txtPassword, "Password");
    }
    return txtPassword;
  }

  public JTextField getTxtBio() {
    if (txtBio.getText().equals("Bio")) {
      setupPlaceholder(txtBio, "Bio");
    }
    return txtBio;
  }

  private void setupPlaceholder(JTextField textField, String placeholder) {
    textField.setForeground(Color.GRAY);
    textField.addFocusListener(
      new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
          if (textField.getText().equals(placeholder)) {
            textField.setText("");
            textField.setForeground(Color.BLACK);
          }
        }

        @Override
        public void focusLost(FocusEvent e) {
          if (textField.getText().isEmpty()) {
            textField.setForeground(Color.GRAY);
            textField.setText(placeholder);
          }
        }
      }
    );
  }

  public void setTxtUsername(JTextField txtUsername) {
    this.txtUsername = txtUsername;
  }

  public void setTxtPassword(JTextField txtPassword) {
    this.txtPassword = txtPassword;
  }

  public void setTxtBio(JTextField txtBio) {
    this.txtBio = txtBio;
  }

  public JButton getBtnRegister() {
    return btnRegister;
  }

  public void setBtnRegister(JButton btnRegister) {
    this.btnRegister = btnRegister;
  }

  public JLabel getLblPhoto() {
    return lblPhoto;
  }

  public void setLblPhoto(JLabel lblPhoto) {
    this.lblPhoto = lblPhoto;
  }

  public JButton getBtnUploadPhoto() {
    return btnUploadPhoto;
  }

  public void setBtnUploadPhoto(JButton btnUploadPhoto) {
    this.btnUploadPhoto = btnUploadPhoto;
  }

  public JButton getButton() {
    return button;
  }

  public void setButton(JButton button) {
    this.button = button;
  }

  public JButton getBtnRegisterNow() {
    return btnRegisterNow;
  }

  public void setBtnRegisterNow(JButton btnRegisterNow) {
    this.btnRegisterNow = btnRegisterNow;
  }

  public User getNewUser() {
    return newUser;
  }

  public void setNewUser(User newUser) {
    this.newUser = newUser;
  }

  public JButton createIconButton(String iconPath, String buttonType) {
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
          openHomeUI();
        });
        break;
      case "explore":
        button.addActionListener(e -> {
          openExploreUI();
        });
        break;
      case "add":
        button.addActionListener(e -> {
          openImageUploadUI();
        });
        break;
      case "notification":
        button.addActionListener(e -> {
          openNotificationsUI();
        });
        break;
      case "profile":
        button.addActionListener(e -> {
          openProfileUI();
        });
        break;
    }

    return button;
  }

  // Open QuackstagramHomeUI frame
  private void openHomeUI() {
    HomePanel homeUI = new HomePanel();
    homeUI.setVisible(true);
  }

  // Open ExploreUI frame
  private void openExploreUI() {
    ExplorePanel explore = ExplorePanel.getInstance();
    explore.setVisible(true);
  }

  // Open ImageUploadUI frame
  private void openImageUploadUI() {
    ImageUploadPanel imageUploadUI = new ImageUploadPanel();
    imageUploadUI.setVisible(true);
  }

  // Open NotificationsUI frame
  private void openNotificationsUI() {
    NotificationsPanel notificationsUI = new NotificationsPanel();
    notificationsUI.setVisible(true);
  }

  // Open InstagramProfileUI frame
  private void openProfileUI() {
    String loggedInUsername = "";

    // Read the logged-in user's username from users.txt
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(users))) {
      String line = reader.readLine();

      if (line != null) {
        loggedInUsername = line.split(":")[0].trim();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    ProfilePanel profileUI = new ProfilePanel(new User(loggedInUsername));
    profileUI.setVisible(true);
  }

  public JPanel getPhotoPanel(JLabel lblPhoto) {
    lblPhoto = new JLabel();
    lblPhoto.setPreferredSize(new Dimension(80, 80));
    lblPhoto.setHorizontalAlignment(JLabel.CENTER);
    lblPhoto.setVerticalAlignment(JLabel.CENTER);
    lblPhoto.setIcon(
      new ImageIcon(
        new ImageIcon(dacsLogo)
          .getImage()
          .getScaledInstance(80, 80, Image.SCALE_SMOOTH)
      )
    );
    JPanel photoPanel = new JPanel();
    photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    photoPanel.add(lblPhoto);
    return photoPanel;
  }

  public void addStruct(
    JPanel photoPanel,
    JPanel fieldsPanel,
    boolean isSignUp
  ) {
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

  public void getPassword() {
    txtPassword.setForeground(Color.GRAY);
  }

  public void getUsername() {
    txtUsername.setForeground(Color.GRAY);
  }

  public void getBio() {
    txtBio.setForeground(Color.GRAY);
  }

  public JPanel getFieldsPanel() {
    JPanel fieldsPanel = new JPanel();
    fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
    fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
    return fieldsPanel;
  }

  public void addComponents(
    JPanel headerPanel,
    JPanel fieldsPanel,
    JPanel registerPanel
  ) {
    add(headerPanel, BorderLayout.NORTH);
    add(fieldsPanel, BorderLayout.CENTER);
    add(registerPanel, BorderLayout.SOUTH);
  }

  public void photoUploadPanel(JPanel fieldsPanel) {
    JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    photoUploadPanel.add(btnUploadPhoto);
    fieldsPanel.add(photoUploadPanel);
  }
}
