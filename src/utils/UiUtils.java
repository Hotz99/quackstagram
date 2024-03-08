package utils;

import java.io.BufferedReader;

import explore.ExploreUI;
import home.QuakstagramHomeUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.*;
import javax.swing.*;

import auth.SignUpUI;
import notifications.NotificationsUI;
import user.InstagramProfileUI;
import user.User;

public class UiUtils {

  // private final int WIDTH = 300;
  // private final int HEIGHT = 500;
  private static final int NAV_ICON_SIZE = 20; // Corrected static size for bottom icons
  // private final int IMAGE_WIDTH = WIDTH - 100; // Width for the image posts
  // private final int IMAGE_HEIGHT = 150; // Height for the image posts
  // private final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for
  // the like button

  public static final String[] iconPaths = {
      AppPaths.ICONS + "home.png",
      AppPaths.ICONS + "search.png",
      AppPaths.ICONS + "add.png",
      AppPaths.ICONS + "heart.png",
      AppPaths.ICONS + "profile.png",
  };

  public static final String[] buttonTypes = {
      "home",
      "explore",
      "add",
      "notification",
      "profile",
  };

  public static JPanel createHeaderPanel(String label, int width, int height) {
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
    JLabel lblRegister = new JLabel(label);
    lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
    lblRegister.setForeground(Color.WHITE); // Set the text color to white
    headerPanel.add(lblRegister);
    headerPanel.setPreferredSize(new Dimension(width, height)); // Give the header a fixed height
    return headerPanel;
  }

  public static JPanel createNavigationPanel(JFrame currentFrame) {
    // Create and return the navigation panel
    // Navigation Bar
    JPanel navigationPanel = new JPanel();
    navigationPanel.setBackground(new Color(249, 249, 249));
    navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
    navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    for (int i = 0; i < iconPaths.length; i++) {
      navigationPanel.add(
          createIconButton(
              currentFrame,
              iconPaths[i],
              buttonTypes[i]));
      navigationPanel.add(Box.createHorizontalGlue());
    }

    return navigationPanel;
  }

  public static JButton createIconButton(JFrame currentFrame, String iconPath, String buttonType) {
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
  public static void openProfileUI() {
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
  public static void notificationsUI() {
    NotificationsUI notificationsUI = new NotificationsUI();
    notificationsUI.setVisible(true);
  }

  // Open InstagramProfileUI frame
  public static void openHomeUI() {
    QuakstagramHomeUI homeUI = new QuakstagramHomeUI();
    homeUI.setVisible(true);
  }

  // Open InstagramProfileUI frame
  public static void exploreUI() {
    ExploreUI explore = new ExploreUI();
    explore.setVisible(true);
  }

   public static JPanel getPhotoPanel(JLabel lblPhoto) {
    // Profile picture placeholder without border
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


    public static JPanel getFieldsPanel(JPanel photoPanel, boolean includeBio) {
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JTextField txtUsername = new JTextField("Username");
        JTextField txtPassword = new JTextField("Password");
        txtUsername.setForeground(Color.GRAY);
        txtPassword.setForeground(Color.GRAY);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);

        if (includeBio) {
            JTextField txtBio = new JTextField("Bio");
            txtBio.setForeground(Color.GRAY);
            fieldsPanel.add(Box.createVerticalStrut(10));
            fieldsPanel.add(txtBio);
        }

        return fieldsPanel;
    }

    
      public static JPanel getRegisterPanel(String label, Color backgroundColor, Color textColor, String fontName, int fontSize, ActionListener actionListener) {
          JButton button = new JButton(label);
          button.addActionListener(actionListener);
          button.setBackground(backgroundColor);
          button.setForeground(textColor);
          button.setFocusPainted(false);
          button.setBorderPainted(false);
          button.setFont(new Font(fontName, Font.BOLD, fontSize));
  
          JPanel registerPanel = new JPanel(new BorderLayout());
          registerPanel.setBackground(Color.WHITE);
          registerPanel.add(button, BorderLayout.CENTER);
  
          return registerPanel;
      }
    
    
}
