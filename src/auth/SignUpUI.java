package auth;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import utils.AppPaths;
import utils.UiUtils;

public class SignUpUI extends JFrame {

  private static final int WIDTH = 300;
  private static final int HEIGHT = 500;
  private static final int HEIGHT_HEADERPANEL = 40; 
  private static final Color BUTTON_TEXT_COLOR = Color.BLACK;
  private static final Color COLOR_WHITE = Color.WHITE;
  private static final Color BUTTON_BACKGROUND_COLOR = new Color(255, 90, 95);
  private static final int SIZE = 16; 
  private static final String FONT_NAME = "Arial";
  private static final String LABEL = "Quackstagram 🐥";
  private static final String REGISTER_LABEL = "Register";

  private JTextField txtUsername;
  private JTextField txtPassword;
  private JTextField txtBio;
  private JButton btnRegister;
  private JLabel lblPhoto;
  private JButton btnUploadPhoto;

  private JButton button;

  public SignUpUI() {
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
    JPanel fieldsPanel = UiUtils.getFieldsPanel(photoPanel, true);
    getPhotoUploadButton(btnUploadPhoto ,fieldsPanel);
    JPanel registerPanel = UiUtils.getRegisterPanel(
      REGISTER_LABEL, 
      BUTTON_BACKGROUND_COLOR, 
      BUTTON_TEXT_COLOR, 
      FONT_NAME, 
      14, 
      this::onRegisterClicked 
  );



    add(headerPanel, BorderLayout.NORTH);
    add(fieldsPanel, BorderLayout.CENTER);
    add(registerPanel, BorderLayout.SOUTH);


    button = new JButton("Already have an account? Sign In");
    button.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            openSignInUI();
          }
        });
    registerPanel.add(button, BorderLayout.SOUTH);
  }






  private void getPhotoUploadButton(JButton btnUploadPhoto, JPanel fieldsPanel) {
    btnUploadPhoto = new JButton("Upload Photo");

    btnUploadPhoto.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            handleProfilePictureUpload();
          }
        });
    JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    photoUploadPanel.add(btnUploadPhoto);
    fieldsPanel.add(photoUploadPanel);
  }

  private void onRegisterClicked(ActionEvent event) {
    String username = txtUsername.getText();
    String password = txtPassword.getText();
    String bio = txtBio.getText();

    if (doesUsernameExist(username)) {
      JOptionPane.showMessageDialog(
          this,
          "Username already exists. Please choose a different username.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    } else {
      saveCredentials(username, password, bio);
      handleProfilePictureUpload();
      dispose();

      SwingUtilities.invokeLater(() -> {
        SignInUI signInFrame = new SignInUI();
        signInFrame.setVisible(true);
      });
    }
  }

  private boolean doesUsernameExist(String username) {
    try (
        BufferedReader reader = new BufferedReader(
            new FileReader(AppPaths.CREDENTIALS))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith(username + ":")) {
          return true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private void handleProfilePictureUpload() {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Image files",
        ImageIO.getReaderFileSuffixes());
    fileChooser.setFileFilter(filter);
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      saveProfilePicture(selectedFile, txtUsername.getText());
    }
  }


  private void saveProfilePicture(File file, String username) {
    try {
      BufferedImage image = ImageIO.read(file);
      File outputFile = new File(AppPaths.PROFILE_IMAGES_STORAGE + username + ".png");
      ImageIO.write(image, "png", outputFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void saveCredentials(String username, String password, String bio) {
    try (
        BufferedWriter writer = new BufferedWriter(
            new FileWriter(
                AppPaths.CREDENTIALS,
                true))) {
      writer.write(username + ":" + password + ":" + bio);
      writer.newLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void openSignInUI() {    
    dispose();

    SwingUtilities.invokeLater(() -> {
      SignInUI signInFrame = new SignInUI();
      signInFrame.setVisible(true);
    });
  }
}
