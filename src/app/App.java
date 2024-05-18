package app;

import auth.SignInPanel;
import auth.SignUpPanel;
import database.repositories.UserRepository;
import explore.ExplorePanel;
import home.HomePanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import notifications.NotificationsPanel;
import post.PostUploadPanel;
import post.PostImageViewer;
import user.ProfilePanel;
import utils.BasePanel;

public class App extends JFrame {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      App app = new App();
      app.setLocationRelativeTo(null);
      app.setVisible(true);
    });
  }

  private static final int WIDTH = 300;
  private static final int HEIGHT = 500;
  private static PostImageViewer imageViewer = new PostImageViewer();
  private static BasePanel imageViewPanel = new BasePanel(true, true, true);
  private static CardLayout cardLayout = new CardLayout();
  private static JPanel cards = new JPanel(cardLayout);
  private static ExplorePanel explorePanel = ExplorePanel.getInstance();

  public static int getAppWidth() {
    return WIDTH;
  }

  public static int getAppHeight() {
    return HEIGHT;
  }

  public static PostImageViewer getPostViewer() {
    return imageViewer;
  }

  public static BasePanel getImageViewPanel() {
    return imageViewPanel;
  }

  public static CardLayout getCardLayout() {
    return cardLayout;
  }

  public static JPanel getCards() {
    return cards;
  }

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

  public static void showPanel(String panelName) {
    explorePanel.closeOverlayComponents();
    cardLayout.show(cards, panelName);
  }

  // called after signin
  public static void addRemainingPanels() {
    cards.add(new HomePanel(), "Home");
    cards.add(explorePanel, "Explore");
    cards.add(new PostUploadPanel(), "Image Upload");
    cards.add(imageViewPanel, "Image View");
    cards.add(new NotificationsPanel(), "Notifications");
  }

  public static void showProfileByUsername(String username) {
    final ProfilePanel profilePanel = new ProfilePanel(UserRepository.getInstance().getByUsername(username));
    // Add the new ProfilePanel
    cards.add(profilePanel, "UserProfile");
    cardLayout.show(cards, "UserProfile");
  }
}
