package user;

import app.App;
import auth.UserManager;
import database.FollowRepository;
import database.PostRepository;
import post.Post;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import utils.BasePanel;

public class ProfilePanel extends BasePanel {

  private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
  private static final int GRID_IMAGE_SIZE = App.WIDTH / 3; // Static size for grid images
  private JPanel contentPanel = new JPanel(); // Panel to display the image grid or the clicked image
  private final UserManager userManager = UserManager.getInstance();
  private JPanel headerPanel = new JPanel(); // Panel for the header

  // user who's profile is being built here
  private User profileUser;

  public ProfilePanel(User user) {
    super(false, false, false);

    this.profileUser = user;

    System.out.println("loading profile: " + user.getUsername());

    initializeUI();
  }

  private void initializeUI() {
    removeAll();
    createHeaderPanel();

    initializeImageGrid();

    revalidate();
    repaint();
  }

  private void createHeaderPanel() {

    headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
    headerPanel.setBackground(Color.GRAY);

    JPanel topHeaderPanel = setTopHeaderPanel();
    JPanel statsPanel = setStatsPanel();
    JButton followButton = createFollowButton();

    getStatsFollowPanel(headerPanel, topHeaderPanel, statsPanel, followButton);
    getProfileBioName(headerPanel);
  }

  private void getProfileBioName(JPanel headerPanel) {
    // Profile Name and Bio Panel
    JPanel profileNameAndBioPanel = new JPanel();
    profileNameAndBioPanel.setLayout(new BorderLayout());
    profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

    JLabel profileNameLabel = new JLabel(profileUser.getUsername());
    profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
    profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides

    JTextArea profileBio = new JTextArea(profileUser.getBio());
    System.out.println("This is the bio for " + profileUser.getUsername());
    profileBio.setEditable(false);
    profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
    profileBio.setBackground(new Color(249, 249, 249));
    profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides

    profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
    profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

    headerPanel.add(profileNameAndBioPanel);

    this.headerPanel = headerPanel;
    add(headerPanel, BorderLayout.NORTH);
  }

  private void getStatsFollowPanel(
      JPanel headerPanel,
      JPanel topHeaderPanel,
      JPanel statsPanel,
      JButton followButton) {
    // Add Stats and Follow Button to a combined Panel
    JPanel statsFollowPanel = new JPanel();
    statsFollowPanel.setLayout(
        new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
    statsFollowPanel.add(statsPanel);
    statsFollowPanel.add(followButton);
    topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

    headerPanel.add(topHeaderPanel);
  }

  private JButton createFollowButton() {
    boolean isCurrentUser = profileUser.getUsername().equals(userManager.getCurrentUser().getUsername());

    JButton followButton;
    if (isCurrentUser) {
      followButton = new JButton("Edit Profile");
    } else {
      followButton = new JButton("Follow");

      // Check if the profile user is already being followed by the logged-in user
      if (FollowRepository.getInstance().doesUserFollowOtherUser(userManager.getCurrentUser().getUserId(),
          profileUser.getUserId())) {
        followButton.setText("Following");
      }

      followButton.addActionListener(e -> {
        FollowRepository.getInstance().toggleFollow(userManager.getCurrentUser().getUserId(), profileUser.getUserId());

        // refresh
        App.showProfileByUsername(profileUser.getUsername());
      });
    }

    followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    followButton.setFont(new Font("Arial", Font.BOLD, 12));
    followButton.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height)); // Make the button fill the horizontal
    // space
    followButton.setBackground(new Color(225, 228, 232));
    followButton.setForeground(Color.BLACK);
    followButton.setOpaque(true);
    followButton.setBorderPainted(false);
    followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some vertical padding
    return followButton;
  }

  private JPanel setStatsPanel() {
    System.out.println("setStatsPanel()");

    // Stats Panel
    JPanel statsPanel = new JPanel();
    statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
    statsPanel.setBackground(new Color(249, 249, 249));
    System.out.println(
        "Number of posts for this user" +
            profileUser.getPostsCount());
    statsPanel.add(
        createStatLabel(
            Integer.toString(profileUser.getPostsCount()),
            "Posts"));
    statsPanel.add(
        createStatLabel(
            Integer.toString(profileUser.getFollowersCount()),
            "Followers"));
    statsPanel.add(
        createStatLabel(
            Integer.toString(profileUser.getFollowingCount()),
            "Following"));
    statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding
    return statsPanel;
  }

  private JPanel setTopHeaderPanel() {
    // Top Part of the Header (Profile Image, Stats, Follow Button)
    JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
    topHeaderPanel.setBackground(new Color(249, 249, 249));

    String imagePath = "resources/images/profile/" + profileUser.getProfileImagePath();

    System.out.println("Profile image path: " + imagePath);

    // Profile image
    Image originalImage = new ImageIcon(imagePath).getImage();

    Image scaledImage = originalImage.getScaledInstance(
        PROFILE_IMAGE_SIZE,
        PROFILE_IMAGE_SIZE,
        Image.SCALE_SMOOTH);
    ImageIcon profileIcon = new ImageIcon(scaledImage);

    JLabel profileImage = new JLabel(profileIcon);
    profileImage.setPreferredSize(
        new Dimension(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE));
    profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    profileImage.setHorizontalAlignment(JLabel.CENTER);
    profileImage.setVerticalAlignment(JLabel.CENTER);

    topHeaderPanel.add(profileImage, BorderLayout.WEST);
    return topHeaderPanel;
  }

  private void setupContentPanel() {
    contentPanel.removeAll();
    contentPanel.setLayout(new GridLayout(0, 3, 5, 5));
  }

  private void loadAndDisplayImages() {
    for (Post post : PostRepository.getInstance().getAllByUserId(profileUser.getUserId())) {
      addImageToPanel(post.getImagePath());
      System.out.println("loaded post with image " + post.getImagePath());
    }
  }

  private void addImageToPanel(String imagePath) {
    ImageIcon imageIcon = new ImageIcon(
        new ImageIcon(imagePath.toString())
            .getImage()
            .getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
    JLabel imageLabel = new JLabel(imageIcon);
    imageLabel.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            displayImage(imageIcon);
          }
        });
    contentPanel.add(imageLabel);
  }

  private JScrollPane createScrollPane() {
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    return scrollPane;
  }

  private void initializeImageGrid() {
    setupContentPanel();
    loadAndDisplayImages();
    JScrollPane scrollPane = createScrollPane();
    add(scrollPane, BorderLayout.CENTER);
    revalidate();
    repaint();
  }

  private void displayImage(ImageIcon imageIcon) {
    contentPanel.removeAll();
    contentPanel.setLayout(new BorderLayout());

    JLabel fullSizeImageLabel = new JLabel(imageIcon);
    fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
    contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

    JButton backButton = new JButton("Back");
    backButton.addActionListener(e -> {
      removeAll();
      initializeUI();
    });
    contentPanel.add(backButton, BorderLayout.SOUTH);

    revalidate();
    repaint();
  }

  private JLabel createStatLabel(String number, String text) {
    JLabel label = new JLabel(
        "<html><div style='text-align: center;'>" +
            number +
            "<br/>" +
            text +
            "</div></html>",
        SwingConstants.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 12));
    label.setForeground(Color.BLACK);
    return label;
  }
}
