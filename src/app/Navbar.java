package app;

import java.awt.Color;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import auth.UserManager;
import utils.AppPathsSingleton;

public class Navbar extends JPanel {

  private final int NAV_ICON_SIZE = 20; // Corrected size for bottom icons
  protected static final Color COLOR_WHITE = Color.WHITE;
  protected static final Color BUTTON_BACKGROUND_COLOR = new Color(255, 90, 95);
  protected static final Color BUTTON_TEXT_COLOR = Color.BLACK;
  protected static final String FONT_NAME = "Arial";
  protected static final String LABEL = "Quackstagram 🐥";

  private final static AppPathsSingleton appPaths = AppPathsSingleton.getInstance();
  private static final String[] iconPaths = appPaths.ICON_PATHS;
  private static final String[] buttonTypes = appPaths.BUTTON_TYPES;

  public Navbar() {
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
          App.showPanel("Home");
        });
        break;
      case "explore":
        button.addActionListener(e -> {
          App.showPanel("Explore");
        });
        break;
      case "add":
        button.addActionListener(e -> {
          App.showPanel("Image Upload");
        });
        break;
      case "notification":
        button.addActionListener(e -> {
          App.showPanel("Notifications");
        });
        break;
      case "profile":
        button.addActionListener(e -> {
          App.showProfileByUsername(UserManager.getInstance().getCurrentUser().getUsername());
        });
        break;
    }

    return button;
  }
}
