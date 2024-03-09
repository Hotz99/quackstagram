package utils;

import java.io.BufferedReader;

import explore.ExploreUI;
import home.QuakstagramHomeUI;
import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import javax.swing.*;

import notifications.NotificationsUI;
import user.InstagramProfileUI;
import user.User;
import utils.BaseFrame;


public class BaseFrame extends JFrame {

  public final int APP_WIDTH = 300;
  public final int APP_HEIGHT = 500;
  private final int NAV_ICON_SIZE = 20; // Corrected size for bottom icons
  private static final int HEADER_HEIGHT = 40; // Corrected static size for bottom icons
  public static final Color COLOR_WHITE = Color.WHITE;
  public static final Color BUTTON_BACKGROUND_COLOR = new Color(255, 90, 95);
  public static final Color BUTTON_TEXT_COLOR = Color.BLACK;
  public static final String FONT_NAME = "Arial";
  public static final String LABEL = "Quackstagram 🐥";
  public static final String SIGNIN_LABEL = "Sign-In";
  public static final String REGISTER_LABEL = "Register";

  public JTextField txtUsername;
  public JTextField txtPassword;
  public JTextField txtBio;
  public JButton btnRegister;
  public JLabel lblPhoto;
  public JButton btnUploadPhoto;
  public JButton button, btnRegisterNow;
  public User newUser;
  //fucked
  public JPanel bioPanel;



  public final String[] iconPaths = {
      AppPaths.ICONS + "home.png",
      AppPaths.ICONS + "search.png",
      AppPaths.ICONS + "add.png",
      AppPaths.ICONS + "heart.png",
      AppPaths.ICONS + "profile.png",
  };

  public final String[] buttonTypes = {
      "home",
      "explore",
      "add",
      "notification",
      "profile",
  };

  public JPanel createHeaderPanel(String label) {
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
    JLabel lblRegister = new JLabel(label);
    lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
    lblRegister.setForeground(Color.WHITE); // Set the text color to white
    headerPanel.add(lblRegister);
    headerPanel.setPreferredSize(new Dimension(APP_WIDTH, HEADER_HEIGHT)); // Give the header a fixed height
    return headerPanel;
  }

  public JPanel createNavigationPanel() {
    // Create and return the navigation panel
    // Navigation Bar
    JPanel navigationPanel = new JPanel();
    navigationPanel.setBackground(new Color(249, 249, 249));
    navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
    navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    for (int i = 0; i < iconPaths.length; i++) {
      navigationPanel.add(
          createIconButton(
              this,
              iconPaths[i],
              buttonTypes[i]));
      navigationPanel.add(Box.createHorizontalGlue());
    }

    return navigationPanel;
  }

  public JButton createIconButton(JFrame currentFrame, String iconPath, String buttonType) {
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
          currentFrame.dispose();
          openHomeUI();
        });
        break;
      case "profile":
        button.addActionListener(e -> {
          currentFrame.dispose();
          openProfileUI();
        });
        break;
      case "notification":
        button.addActionListener(e -> {
          currentFrame.dispose();
          notificationsUI();
        });
        break;
      case "explore":
        button.addActionListener(e -> {
          currentFrame.dispose();
          exploreUI();
        });
        break;
    }

    return button;
  }

  // Open InstagramProfileUI frame
  public void openProfileUI() {
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

  // Open InstagramProfileUI frame
  public void notificationsUI() {
    NotificationsUI notificationsUI = new NotificationsUI();
    notificationsUI.setVisible(true);
  }

  // Open InstagramProfileUI frame
  public void openHomeUI() {
    QuakstagramHomeUI homeUI = new QuakstagramHomeUI();
    homeUI.setVisible(true);
  }

  // Open InstagramProfileUI frame
  public void exploreUI() {
    ExploreUI explore = new ExploreUI();
    explore.setVisible(true);
  }

   public JPanel getPhotoPanel(JLabel lblPhoto) {    
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




  

  public void addStruct(JPanel photoPanel, JPanel fieldsPanel, boolean isSignUp) {
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
    txtPassword = new JTextField("Password");
    txtPassword.setForeground(Color.GRAY);
  }

  public void getUsername() {
    txtUsername = new JTextField("Username");
    txtUsername.setForeground(Color.GRAY);
  }

  public void getBio() {
    txtBio = new JTextField("Bio");
    txtBio.setForeground(Color.GRAY);
  }


  public JPanel getFieldsPanel() {
    JPanel fieldsPanel = new JPanel();
    fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
    fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
    return fieldsPanel;
  }


  public void addComponents(JPanel headerPanel, JPanel fieldsPanel, JPanel registerPanel) {
    add(headerPanel, BorderLayout.NORTH);
    add(fieldsPanel, BorderLayout.CENTER);
    add(registerPanel, BorderLayout.SOUTH);
  }
  

  // public JPanel registerPanel() {
  //   JPanel registerPanel = new JPanel(new BorderLayout()); 
  //   registerPanel.setBackground(Color.WHITE);
  //   registerPanel.add(button, BorderLayout.CENTER);
  //   return registerPanel;
  // }

  public void photoUploadPanel(JPanel fieldsPanel) {
    JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    photoUploadPanel.add(btnUploadPhoto);
    fieldsPanel.add(photoUploadPanel);
  }

  
}
