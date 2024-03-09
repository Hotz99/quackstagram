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
import utils.BaseFrame;

public class SignUpUI extends BaseFrame { 

  public SignUpUI() {
    setTitle("Quackstagram - Register");
    setSize(APP_WIDTH, APP_HEIGHT);
    setMinimumSize(new Dimension(APP_WIDTH, APP_HEIGHT));
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));
    initializeUI();
  }

  private void initializeUI() {
    JPanel headerPanel = createHeaderPanel(LABEL);
    JPanel photoPanel = getPhotoPanel(lblPhoto);
    JPanel fieldsPanel = getFieldsPanel();
    getUsername();
    getPassword();
    getBio();
    addStruct(photoPanel, fieldsPanel, true);
    btnUploadImage();
    photoUploadPanel(fieldsPanel);
    btnRegister(REGISTER_LABEL);
    JPanel registerPanel = registerPanel();
    addComponents(headerPanel, fieldsPanel, registerPanel);
    signInButton(registerPanel);
  }

  private void signInButton(JPanel registerPanel) {
    button = new JButton("Already have an account? Sign In");
    button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            openSignInUI();
        }
    });
    registerPanel.add(button, BorderLayout.SOUTH);
  }



  private JPanel registerPanel() {
    JPanel registerPanel = new JPanel(new BorderLayout());
    registerPanel.setBackground(Color.WHITE); 
    registerPanel.add(btnRegister, BorderLayout.CENTER);
    return registerPanel;
  }

  private void btnRegister(String label) {
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
