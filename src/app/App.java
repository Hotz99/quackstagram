package app;

import auth.SignInPanel;
import auth.SignUpPanel;
import auth.UserManager;
import explore.ExplorePanel;
import home.HomePanel;
import image.ImageUploadPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import notifications.NotificationsPanel;
import user.ProfilePanel;

public class App extends JFrame {

  public static final int WIDTH = 300;
  public static final int HEIGHT = 500;

  private static CardLayout cardLayout = new CardLayout();
  private static JPanel cards = new JPanel(cardLayout);

  public static UserManager userManager = new UserManager();

  public App() {
    setTitle("Quackstagram");
    setSize(WIDTH, HEIGHT);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // Add panels to card layout
    cards.add(new SignUpPanel(), "SignUp");
    cards.add(new HomePanel(), "Home");
    cards.add(new ExplorePanel(), "Explore");
    cards.add(new ImageUploadPanel(), "Image Upload");
    cards.add(new NotificationsPanel(), "Notifications");

    // this also throws
    cards.add(new ProfilePanel(), "Profile");

    // this throws when the addStruct() in the SignIn ctor is called
    cards.add(new SignInPanel(), "SignIn");

    add(cards, BorderLayout.CENTER);
    add(new Navbar(), BorderLayout.SOUTH);

    // Show the initial panel
    cardLayout.show(cards, "SignIn");
  }

  public static void showPanel(String panelName) {
    cardLayout.show(cards, panelName);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      App app = new App();
      app.setLocationRelativeTo(null);
      app.setVisible(true);
    });
  }
}
