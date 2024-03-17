package app;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import utils.AppPaths;

public class Navbar extends JPanel {

  private final int NAV_ICON_SIZE = 20; // Corrected size for bottom icons
  protected static final Color COLOR_WHITE = Color.WHITE;
  protected static final Color BUTTON_BACKGROUND_COLOR = new Color(255, 90, 95);
  protected static final Color BUTTON_TEXT_COLOR = Color.BLACK;
  protected static final String FONT_NAME = "Arial";
  protected static final String LABEL = "Quackstagram 🐥";

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

  public Navbar() {
    // Create and return the navigation panel
    // Navigation Bar
    setBackground(new Color(249, 249, 249));
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    for (int i = 0; i < iconPaths.length; i++) {
      add(createIconButton(iconPaths[i], buttonTypes[i]));

      if (i < iconPaths.length - 1) {
        add(Box.createHorizontalGlue());
      }
    }
  }

  private JButton createIconButton(String iconPath, String buttonType) {
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
          System.out.println("Home panel clicked");
          App.showPanel("Home");
        });
        break;
      case "explore":
        button.addActionListener(e -> {
          System.out.println("Explore panel clicked");
          App.showPanel("Explore");
        });
        break;
      case "add":
        button.addActionListener(e -> {
          System.out.println("Image Upload panel clicked");
          App.showPanel("Image Upload");
        });
        break;
      case "notification":
        button.addActionListener(e -> {
          System.out.println("Notifications panel clicked");
          App.showPanel("Notifications");
        });
        break;
      case "profile":
        button.addActionListener(e -> {
          System.out.println("Profile panel clicked");
          App.showPanel("Profile");
        });
        break;
    }

    return button;
  }
}
