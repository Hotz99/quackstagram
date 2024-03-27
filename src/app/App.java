package app;

import auth.SignInPanel;
import auth.SignUpPanel;
import auth.UserManager;
import explore.ExplorePanel;
import home.HomePanel;
import image.ImageUploadPanel;
import image.ImageViewer;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import notifications.NotificationsPanel;
import user.ProfilePanel;
import user.User;
import utils.BasePanel;

public class App extends JFrame {

  public static final int WIDTH = 300;
  public static final int HEIGHT = 500;

  private static CardLayout cardLayout = new CardLayout();
  private static JPanel cards = new JPanel(cardLayout);

  public static UserManager userManager = UserManager.getInstance();
  public static ImageViewer imageViewer = new ImageViewer();
  public static BasePanel imageView = new BasePanel(true, true, true);

  public App() {
    setTitle("Quackstagram");
    setSize(WIDTH, HEIGHT);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    cards.add(new SignUpPanel(), "SignUp");
    cards.add(new SignInPanel(), "SignIn");

    add(cards, BorderLayout.CENTER);
    add(new Navbar(), BorderLayout.SOUTH);

    // Show the initial panel
    cardLayout.show(cards, "SignIn");
  }

  public static void createPanels() {
    cards.add(new HomePanel(), "Home");
    cards.add(ExplorePanel.getInstance(), "Explore");
    cards.add(new ImageUploadPanel(), "Image Upload");
    cards.add(imageView, "Image View");
    cards.add(new NotificationsPanel(), "Notifications");
    cards.add(new ProfilePanel(), "Profile");
  }

  public static void showPanel(String panelName) {
    cardLayout.show(cards, panelName);
    ExplorePanel.getInstance().closeOverlayComponents();
  }

  public static void showPanelWithUsername(String username) {
    final User user = new User(username);
    final ProfilePanel profilePanel = new ProfilePanel(user);
    // Add the new ProfilePanel
    cards.add(profilePanel, "UserProfile");
    cardLayout.show(cards, "UserProfile");
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      App app = new App();
      app.setLocationRelativeTo(null);
      app.setVisible(true);
    });
  }

  public static JPanel getCards() {
    return cards;
  }
}
