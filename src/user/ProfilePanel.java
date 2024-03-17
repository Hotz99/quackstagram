package user;

import app.App;
import auth.UserManager;
import explore.ExplorePanel;
import image.ImageUploadPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;
import javax.swing.*;
import notifications.NotificationsPanel;
import utils.AppPaths;
import utils.BasePanel;

public class ProfilePanel extends BasePanel {

  private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
  private static final int GRID_IMAGE_SIZE = App.WIDTH / 3; // Static size for grid images
  private JPanel contentPanel = new JPanel(); // Panel to display the image grid or the clicked image
  String currentUsername = UserManager.getCurrentUser().getUsername();
  private JPanel headerPanel = new JPanel(); // Panel for the header

  public ProfilePanel(User user) {
    UserManager.setCurrentUser(user);

    UserManager.getCurrentUser().loadPostsCount();

    UserManager.getCurrentUser().loadFollowsCount();

    UserManager.getCurrentUser().loadBio();

    // Is the first issue, after that line 194 comes up, if we dont deal with the
    // getPostsCOunt method
    System.out.println(UserManager.getCurrentUser().getPostsCount());

    initializeUI();
  }

  public ProfilePanel() {
    if (UserManager.getCurrentUser() == null) {
      // If no user is logged in, show the sign-in panel
      System.out.println("No user is logged in");
      App.showPanel("SignIn");
      return;
    }
    initializeUI();
  }

  private void initializeUI() {
    removeAll(); // Clear existing components

    createHeaderPanel();

    // Initialize the image grid
    initializeImageGrid();

    revalidate();
    repaint();
  }

  private void createHeaderPanel() {
    boolean isCurrentUser = false;

    // Header Panel
    JPanel headerPanel = new JPanel();
    try (Stream<String> lines = Files.lines(Paths.get(AppPaths.USERS))) {
      isCurrentUser = lines.anyMatch(line -> line.startsWith(currentUsername + ":"));
    } catch (IOException e) {
      e.printStackTrace(); // Log or handle the exception as appropriate
    }

    headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
    headerPanel.setBackground(Color.GRAY);

    JPanel topHeaderPanel = setTopHeaderPanel();
    JPanel statsPanel = setStatsPanel();
    JButton followButton = setUpFollowButton(isCurrentUser);
    getStatsFollowPanel(headerPanel, topHeaderPanel, statsPanel, followButton);
    getProfileBioName(headerPanel);
  }

  private void getProfileBioName(JPanel headerPanel) {
    // Profile Name and Bio Panel
    JPanel profileNameAndBioPanel = new JPanel();
    profileNameAndBioPanel.setLayout(new BorderLayout());
    profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

    JLabel profileNameLabel = new JLabel(currentUsername);
    profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
    profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides

    JTextArea profileBio = new JTextArea(UserManager.getCurrentUser().getBio());
    System.out.println("This is the bio " + currentUsername);
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

  private void getStatsFollowPanel(JPanel headerPanel, JPanel topHeaderPanel, JPanel statsPanel, JButton followButton) {
    // Add Stats and Follow Button to a combined Panel
    JPanel statsFollowPanel = new JPanel();
    statsFollowPanel.setLayout(
        new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
    statsFollowPanel.add(statsPanel);
    statsFollowPanel.add(followButton);
    topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

    headerPanel.add(topHeaderPanel);
  }

  private JButton setUpFollowButton(boolean isCurrentUser) {
    // Follow Button
    // Follow or Edit Profile Button
    // followButton.addActionListener(e ->
    // handleFollowAction(currentUsername));
    JButton followButton;
    if (isCurrentUser) {
      followButton = new JButton("Edit Profile");
    } else {
      followButton = new JButton("Follow");

      // Check if the current user is already being followed by the logged-in user
      Path followingFilePath = Paths.get(AppPaths.FOLLOWING);
      try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
        String line;
        while ((line = reader.readLine()) != null) {
          String[] parts = line.split(":");
          if (parts[0].trim().equals(currentUsername)) {
            String[] followedUsers = parts[1].split(";");
            for (String followedUser : followedUsers) {
              if (followedUser.trim().equals(currentUsername)) {
                followButton.setText("Following");
                break;
              }
            }
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      followButton.addActionListener(e -> {
        handleFollowAction(currentUsername);
        followButton.setText("Following");
      });
    }

    followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    followButton.setFont(new Font("Arial", Font.BOLD, 12));
    followButton.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height)); // Make the button fill the horizontal
    // space
    followButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color that complements the UI
    followButton.setForeground(Color.BLACK);
    followButton.setOpaque(true);
    followButton.setBorderPainted(false);
    followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some vertical padding
    return followButton;
  }

  private JPanel setStatsPanel() {
    // Stats Panel
    JPanel statsPanel = new JPanel();
    statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
    statsPanel.setBackground(new Color(249, 249, 249));
    System.out.println(
        "Number of posts for this user" +
            UserManager.getCurrentUser().getPostsCount());
    statsPanel.add(
        createStatLabel(
            Integer.toString(UserManager.getCurrentUser().getPostsCount()),
            "Posts"));
    statsPanel.add(
        createStatLabel(
            Integer.toString(UserManager.getCurrentUser().getFollowersCount()),
            "Followers"));
    statsPanel.add(
        createStatLabel(
            Integer.toString(UserManager.getCurrentUser().getFollowingCount()),
            "Following"));
    statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding
    return statsPanel;
  }

  private JPanel setTopHeaderPanel() {
    // Top Part of the Header (Profile Image, Stats, Follow Button)
    JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
    topHeaderPanel.setBackground(new Color(249, 249, 249));

    String imagePath = AppPaths.PROFILE_IMAGES_STORAGE + currentUsername + ".png";
    // Profile image
    ImageIcon originalIcon = new ImageIcon(imagePath);
    Image originalImage = originalIcon.getImage();

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

  // --------------------------

  // private void handleFollowAction(String usernameToFollow) {
  // Path followingFilePath = Paths.get(AppPaths.FOLLOWING);
  // Path usersFilePath = Paths.get(AppPaths.USERS);
  // try {
  // // Read the current user's username from users.txt
  // try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
  // String line;
  // while ((line = reader.readLine()) != null) {
  // String[] parts = line.split(":");
  // currentUsername = parts[0];
  // }
  // }

  // System.out.println("Real user is " + currentUsername);
  // // If currentUsername is not empty, process following.txt
  // if (!currentUsername.isEmpty()) {
  // boolean found = false;
  // StringBuilder newContent = new StringBuilder();

  // // Read and process following.txt
  // if (Files.exists(followingFilePath)) {
  // try (
  // BufferedReader reader = Files.newBufferedReader(followingFilePath)
  // ) {
  // String line;
  // while ((line = reader.readLine()) != null) {
  // String[] parts = line.split(":");
  // if (parts[0].trim().equals(currentUsername)) {
  // found = true;
  // if (!line.contains(usernameToFollow)) {
  // line =
  // line
  // .concat(line.endsWith(":") ? "" : "; ")
  // .concat(usernameToFollow);
  // }
  // }
  // newContent.append(line).append("\n");
  // }
  // }
  // }

  // // If the current user was not found in following.txt, add them
  // if (!found) {
  // newContent
  // .append(currentUsername)
  // .append(": ")
  // .append(usernameToFollow)
  // .append("\n");
  // }

  // // Write the updated content back to following.txt
  // try (
  // BufferedWriter writer = Files.newBufferedWriter(followingFilePath)
  // ) {
  // writer.write(newContent.toString());
  // }
  // }
  // } catch (IOException e) {
  // e.printStackTrace();
  // }
  // }

  private String readCurrentUser(Path usersFilePath) {
    try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(":");
        if (parts.length > 0) {
          // Assuming the last line in users.txt is the current user
          return parts[0];
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  private StringBuilder processFollowingFile(Path followingFilePath, String currentUsername, String usernameToFollow)
      throws IOException {
    boolean found = false;
    StringBuilder newContent = new StringBuilder();

    if (Files.exists(followingFilePath)) {
      try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
        String line;
        while ((line = reader.readLine()) != null) {
          String[] parts = line.split(":");
          if (parts[0].trim().equals(currentUsername)) {
            found = true;
            if (!line.contains(usernameToFollow)) {
              line = line.concat(line.endsWith(":") ? "" : "; ").concat(usernameToFollow);
            }
          }
          newContent.append(line).append("\n");
        }
      }
    }

    if (!found) {
      newContent.append(currentUsername).append(": ").append(usernameToFollow).append("\n");
    }

    return newContent;
  }

  private void writeFollowingFile(Path followingFilePath, StringBuilder newContent) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(followingFilePath)) {
      writer.write(newContent.toString());
    }
  }

  private void handleFollowAction(String usernameToFollow) {
    Path followingFilePath = Paths.get(AppPaths.FOLLOWING);
    Path usersFilePath = Paths.get(AppPaths.USERS);
    String currentUsername = readCurrentUser(usersFilePath);

    if (!currentUsername.isEmpty()) {
      try {
        StringBuilder newContent = processFollowingFile(followingFilePath, currentUsername, usernameToFollow);
        writeFollowingFile(followingFilePath, newContent);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // --------------------------

  // private void initializeImageGrid() {
  // contentPanel.removeAll(); // Clear existing content
  // contentPanel.setLayout(new GridLayout(0, 3, 5, 5)); // Grid layout for image
  // grid

  // Path imageDir = Paths.get(AppPaths.UPLOADED);
  // try (Stream<Path> paths = Files.list(imageDir)) {
  // paths
  // .filter(path ->
  // path.getFileName().toString().startsWith(currentUsername + "_")
  // )
  // .forEach(path -> {
  // ImageIcon imageIcon = new ImageIcon(
  // new ImageIcon(path.toString())
  // .getImage()
  // .getScaledInstance(
  // GRID_IMAGE_SIZE,
  // GRID_IMAGE_SIZE,
  // Image.SCALE_SMOOTH
  // )
  // );
  // JLabel imageLabel = new JLabel(imageIcon);
  // imageLabel.addMouseListener(
  // new MouseAdapter() {
  // @Override
  // public void mouseClicked(MouseEvent e) {
  // displayImage(imageIcon); // Call method to display the clicked image
  // }
  // }
  // );
  // contentPanel.add(imageLabel);
  // });
  // } catch (IOException ex) {
  // ex.printStackTrace();
  // // Handle exception (e.g., show a message or log)
  // }

  // JScrollPane scrollPane = new JScrollPane(contentPanel);
  // scrollPane.setHorizontalScrollBarPolicy(
  // JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
  // );
  // scrollPane.setVerticalScrollBarPolicy(
  // JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
  // );

  // add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center

  // revalidate();
  // repaint();
  // }

  private void configureContentPanel() {
    contentPanel.removeAll();
    contentPanel.setLayout(new GridLayout(0, 3, 5, 5));
  }

  private void loadAndDisplayImages() {
    Path imageDir = Paths.get(AppPaths.UPLOADED);
    try (Stream<Path> paths = Files.list(imageDir)) {
      paths.filter(path -> path.getFileName().toString().startsWith(currentUsername + "_"))
          .forEach(this::addImageToPanel);
    } catch (IOException ex) {
      ex.printStackTrace(); // Consider more user-friendly error handling
    }
  }

  private void addImageToPanel(Path imagePath) {
    ImageIcon imageIcon = new ImageIcon(
        new ImageIcon(imagePath.toString()).getImage().getScaledInstance(
            GRID_IMAGE_SIZE,
            GRID_IMAGE_SIZE,
            Image.SCALE_SMOOTH));
    JLabel imageLabel = new JLabel(imageIcon);
    imageLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        displayImage(imageIcon); // Adapt this if necessary to match your displayImage method's parameters
      }
    });
    contentPanel.add(imageLabel);
  }

  private JScrollPane createScrollPane() {
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    return scrollPane;
  }

  private void initializeImageGrid() {
    configureContentPanel();
    loadAndDisplayImages();
    JScrollPane scrollPane = createScrollPane();
    add(scrollPane, BorderLayout.CENTER);
    revalidate();
    repaint();
  }

  // --------------------------

  private void displayImage(ImageIcon imageIcon) {
    contentPanel.removeAll(); // Remove existing content
    contentPanel.setLayout(new BorderLayout()); // Change layout for image display

    JLabel fullSizeImageLabel = new JLabel(imageIcon);
    fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
    contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

    JButton backButton = new JButton("Back");
    backButton.addActionListener(e -> {
      removeAll(); // Remove all components from the frame
      initializeUI(); // Re-initialize the UI
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
