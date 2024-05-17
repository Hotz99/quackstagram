package utils;

import auth.UserManager;
import database.models.Like;
import database.models.Post;
import database.repositories.FollowRepository;
import database.repositories.LikeRepository;
import database.repositories.NotificationRepository;
import database.repositories.PostRepository;
import database.repositories.UserRepository;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import utils.BasePanel;

public class BasePanel extends JPanel {
  protected final FollowRepository followRepo = FollowRepository.getInstance();
  protected final LikeRepository likeRepo = LikeRepository.getInstance();
  protected final NotificationRepository notificationRepo = NotificationRepository.getInstance();
  protected final PostRepository postRepo = PostRepository.getInstance();
  protected final UserRepository userRepo = UserRepository.getInstance();
  protected final AppPathsSingleton appPaths = AppPathsSingleton.getInstance();
  protected final UserManager userManager = UserManager.getInstance();

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

  protected void refresh() {
    System.out.println(this.getClass().getName() + " refresh called");
    this.repaint();
    this.revalidate();
  }

  protected void handleProfilePictureUpload() {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Image files",
        ImageIO.getReaderFileSuffixes());
    fileChooser.setFileFilter(filter);
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      userManager.saveProfilePicture(fileChooser.getSelectedFile());
    }
  }

  protected JTextField getUsernameInputField() {
    if (txtUsername.getText().equals("Username")) {
      setupPlaceholder(txtUsername, "Username");
    }
    return txtUsername;
  }

  protected JTextField getPasswordInputField() {
    if (txtPassword.getText().equals("Password")) {
      setupPlaceholder(txtPassword, "Password");
    }
    return txtPassword;
  }

  protected JTextField getTxtBio() {
    if (txtBio.getText().equals("Bio")) {
      setupPlaceholder(txtBio, "Bio");
    }
    return txtBio;
  }

  private void setupPlaceholder(JTextField textField, String placeholder) {
    textField.setForeground(Color.GRAY);
    textField.addFocusListener(new PlaceholderObserver(placeholder));
  }

  protected void setTxtUsername(JTextField txtUsername) {
    this.txtUsername = txtUsername;
  }

  protected void setTxtPassword(JTextField txtPassword) {
    this.txtPassword = txtPassword;
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

  protected JPanel getPhotoPanel(JLabel lblPhoto) {
    lblPhoto = new JLabel();
    lblPhoto.setPreferredSize(new Dimension(80, 80));
    lblPhoto.setHorizontalAlignment(JLabel.CENTER);
    lblPhoto.setVerticalAlignment(JLabel.CENTER);
    lblPhoto.setIcon(
        new ImageIcon(
            new ImageIcon(appPaths.DACS_LOGO)
                .getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
    JPanel photoPanel = new JPanel();
    photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    photoPanel.add(lblPhoto);
    return photoPanel;
  }

  protected void addStruct(
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

  protected void setupPasswordInput() {
    txtPassword.setForeground(Color.GRAY);
  }

  protected void setupUsernameInput() {
    txtUsername.setForeground(Color.GRAY);
  }

  protected void getBio() {
    txtBio.setForeground(Color.GRAY);
  }

  protected JPanel getFieldsPanel() {
    JPanel fieldsPanel = new JPanel();
    fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
    fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
    return fieldsPanel;
  }

  protected void addComponents(
      JPanel headerPanel,
      JPanel fieldsPanel,
      JPanel registerPanel) {
    add(headerPanel, BorderLayout.NORTH);
    add(fieldsPanel, BorderLayout.CENTER);
    add(registerPanel, BorderLayout.SOUTH);
  }
}
