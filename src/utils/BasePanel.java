package utils;

import auth.UserManager;
import database.users.UserRepository;
import explore.ExplorePanel;
import home.HomePanel;

import java.awt.*;
import javax.swing.*;
import notifications.NotificationsPanel;
import post.PostUploadPanel;
import user.ProfilePanel;
import user.User;
import utils.BasePanel;

public class BasePanel extends JPanel {

  private final int NAV_ICON_SIZE = 20;
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
      "No Account? Register Now");

  private final AppPathsSingleton appPathsSingleton = AppPathsSingleton.getInstance();
  private final String dacsLogo = appPathsSingleton.DACS_LOGO;

  public BasePanel(
      boolean includeUsername,
      boolean includePassword,
      boolean includeBio) {
    if (includeUsername) {
      add(getUsernameInputField());
      setupPlaceholder(txtUsername, "Username");
    }
    if (includePassword) {
      add(getPasswordInputField());
      setupPlaceholder(txtPassword, "Password");
    }
    if (includeBio) {
      add(getTxtBio());
      setupPlaceholder(txtBio, "Bio");
    }
  }

  public JTextField getUsernameInputField() {
    if (txtUsername.getText().equals("Username")) {
      setupPlaceholder(txtUsername, "Username");
    }
    return txtUsername;
  }

  public JTextField getPasswordInputField() {
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
    textField.addFocusListener(new PlaceholderObserver(placeholder));
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

  public JPanel getPhotoPanel(JLabel lblPhoto) {
    lblPhoto = new JLabel();
    lblPhoto.setPreferredSize(new Dimension(80, 80));
    lblPhoto.setHorizontalAlignment(JLabel.CENTER);
    lblPhoto.setVerticalAlignment(JLabel.CENTER);
    lblPhoto.setIcon(
        new ImageIcon(
            new ImageIcon(dacsLogo)
                .getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
    JPanel photoPanel = new JPanel();
    photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    photoPanel.add(lblPhoto);
    return photoPanel;
  }

  public void addStruct(
      JPanel photoPanel,
      JPanel fieldsPanel,
      boolean isSignUp) {
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

  public void setupPasswordInput() {
    txtPassword.setForeground(Color.GRAY);
  }

  public void setupUsernameInput() {
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
      JPanel registerPanel) {
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
