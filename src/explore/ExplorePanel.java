package explore;

import app.App;
import auth.UserManager;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.*;
import user.ProfilePanel;
import user.User;
import utils.*;

/**
 * The ExplorePanel class represents a panel that displays the explore functionality of the application.
 * It extends the BasePanel class and provides methods to create the main content panel, search panel, image grid panel,
 * and other UI components related to exploring and displaying images.
 */
public class ExplorePanel extends BasePanel {

  private final int IMAGE_SIZE = App.WIDTH / 3; // Size for each image in the grid

  /**
   * The ExplorePanel class represents a panel that displays the explore functionality in the application.
   * It contains a header panel and a main content panel.
   */
  public ExplorePanel() {
    removeAll(); // Clear existing components
    setLayout(new BorderLayout()); // Reset the layout manager

    JPanel headerPanel = createHeaderPanel("Explore 🐥");
    JPanel mainContentPanel = createMainContentPanel();

    // Add panels to the frame
    add(headerPanel, BorderLayout.NORTH);
    add(mainContentPanel, BorderLayout.CENTER);

    revalidate();
    repaint();
  }

  /**
   * Creates and returns a JPanel that serves as the main content panel.
   * The main content panel contains a search panel and an image grid panel
   * wrapped in a scroll pane.
   *
   * @return The main content panel.
   */
  private JPanel createMainContentPanel() {
    JPanel searchPanel = createSearchPanel();
    JPanel imageGridPanel = createImageGridPanel();
    JScrollPane scrollPane = new JScrollPane(imageGridPanel);
    scrollPane.setHorizontalScrollBarPolicy(
      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );
    scrollPane.setVerticalScrollBarPolicy(
      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );

    JPanel mainContentPanel = new JPanel();
    mainContentPanel.setLayout(
      new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS)
    );
    mainContentPanel.add(searchPanel);
    mainContentPanel.add(scrollPane); // This will stretch to take up remaining space
    return mainContentPanel;
  }

  /**
   * Creates a JPanel for the search functionality.
   *
   * @return The created JPanel.
   */
  private JPanel createSearchPanel() {
    JPanel searchPanel = new JPanel(new BorderLayout());
    JTextField searchField = new JTextField(" Search Users");
    searchPanel.add(searchField, BorderLayout.CENTER);
    searchPanel.setMaximumSize(
      new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height)
    );
    return searchPanel;
  }

  /**
   * A JPanel is a container that holds and organizes other components.
   * It is used to group and layout other components within a graphical user interface.
   */
  private JPanel createImageGridPanel() {
    JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2)); // 3 columns, auto rows
    File imageDir = new File(AppPaths.UPLOADED);

    if (imageDir.exists() && imageDir.isDirectory()) {
      File[] imageFiles = imageDir.listFiles((dir, name) ->
        name.matches(".*\\.(png|jpg|jpeg)")
      );
      if (imageFiles != null) {
        for (File imageFile : imageFiles) {
          ImageIcon imageIcon = new ImageIcon(
            new ImageIcon(imageFile.getPath())
              .getImage()
              .getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH)
          );
          JLabel imageLabel = new JLabel(imageIcon);
          imageLabel.addMouseListener(
            new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
                displayImage(imageFile.getPath()); // Call method to display the clicked image
              }
            }
          );
          imageGridPanel.add(imageLabel);
        }
      }
    }
    return imageGridPanel;
  }

  /**
   * Displays the image with the given image path.
   *
   * @param imagePath the path of the image to be displayed
   */
  public void displayImage(String imagePath) {
    String imageId = extractImageId(imagePath);
    ImageDetails imageDetails = readImageDetails(imageId);

    if (imageDetails == null) {
      System.out.println("No image details found for ImageID: " + imageId);
      return;
    }

    removeAll();
    setLayout(new BorderLayout());
    add(createHeaderPanel("Explore 🐥"), BorderLayout.NORTH);

    String timeSincePosting = calculateTimeSincePosting(
      imageDetails.getTimestampString()
    );
    JPanel topPanel = createTopPanel(
      imageDetails.getUsername(),
      timeSincePosting
    );
    JLabel imageLabel = prepareImageLabel(imagePath);

    JPanel bottomPanel = createBottomPanel(
      imageDetails.getBio(),
      imageDetails.getLikes()
    );

    JPanel containerPanel = new JPanel(new BorderLayout());
    containerPanel.add(topPanel, BorderLayout.NORTH);
    containerPanel.add(imageLabel, BorderLayout.CENTER);
    containerPanel.add(bottomPanel, BorderLayout.SOUTH);

    add(containerPanel, BorderLayout.CENTER);
    add(createBackButtonPanel(), BorderLayout.SOUTH);

    //make the image likeable
    imageLabel.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) {
            System.out.println("Liked image");
            imageDetails.toggleLike(
              imageId,
              UserManager.getCurrentUser().getUsername()
            );
            // Update the likes label
            int likes = imageDetails.getLikes();
            ((JLabel) bottomPanel.getComponent(1)).setText("Likes: " + likes);
          }
        }
      }
    );

    revalidate();
    repaint();
  }

  /**
   * Extracts the image ID from the given image path.
   *
   * @param imagePath the path of the image
   * @return the image ID extracted from the image path
   */
  private String extractImageId(String imagePath) {
    return new File(imagePath).getName().split("\\.")[0];
  }

  /**
   * Represents the details of an image.
   */
  private ImageDetails readImageDetails(String imageId) {
    Path detailsPath = Paths.get(AppPaths.IMAGE_DETAILS);
    try (Stream<String> lines = Files.lines(detailsPath)) {
      String details = lines
        .filter(line -> line.contains("ImageID: " + imageId))
        .findFirst()
        .orElse("");

      if (details.isEmpty()) {
        return null;
      }

      return new ImageDetails(details);
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * Calculates the time since a post was made based on the given timestamp.
   *
   * @param timestampString the timestamp of the post in the format "yyyy-MM-dd HH:mm:ss"
   * @return a string representing the time since the post was made, e.g. "2 days ago"
   */
  private String calculateTimeSincePosting(String timestampString) {
    if (timestampString.isEmpty()) {
      return "Unknown";
    }
    LocalDateTime timestamp = LocalDateTime.parse(
      timestampString,
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    );
    LocalDateTime now = LocalDateTime.now();
    long days = ChronoUnit.DAYS.between(timestamp, now);
    return days + " day" + (days != 1 ? "s" : "") + " ago";
  }

  /**
   * Creates a JPanel for the top panel of the ExplorePanel.
   *
   * @param username the username of the user
   * @param timeSincePosting the time since the post was made
   * @return the created JPanel
   */
  private JPanel createTopPanel(String username, String timeSincePosting) {
    JPanel topPanel = new JPanel(new BorderLayout());
    JButton usernameLabel = new JButton(username);
    JLabel timeLabel = new JLabel(timeSincePosting);
    timeLabel.setHorizontalAlignment(JLabel.RIGHT);
    topPanel.add(usernameLabel, BorderLayout.WEST);
    topPanel.add(timeLabel, BorderLayout.EAST);

    usernameLabel.addActionListener(e -> {
      User user = new User(username);
      ProfilePanel profileUI = new ProfilePanel(user);
      profileUI.setVisible(true);
    });

    return topPanel;
  }

  /**
   * Creates and returns a JLabel with the specified image.
   *
   * @param imagePath the path to the image file
   * @return a JLabel with the specified image, or a label with "Image not found" text if the image file is not found
   */
  private JLabel prepareImageLabel(String imagePath) {
    JLabel imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    try {
      BufferedImage originalImage = ImageIO.read(new File(imagePath));
      ImageIcon imageIcon = new ImageIcon(originalImage);
      imageLabel.setIcon(imageIcon);
    } catch (IOException ex) {
      imageLabel.setText("Image not found");
    }

    return imageLabel;
  }

  /**
   * Creates a JPanel with a bio text area and a likes label.
   *
   * @param bio   the bio text to display in the text area
   * @param likes the number of likes to display in the likes label
   * @return the created JPanel
   */
  private JPanel createBottomPanel(String bio, int likes) {
    JPanel bottomPanel = new JPanel(new BorderLayout());
    JTextArea bioTextArea = new JTextArea(bio);
    bioTextArea.setEditable(false);
    JLabel likesLabel = new JLabel("Likes: " + likes);
    bottomPanel.add(bioTextArea, BorderLayout.CENTER);
    bottomPanel.add(likesLabel, BorderLayout.SOUTH);

    return bottomPanel;
  }

  /**
   * Creates a JPanel for the back button.
   *
   * @return the JPanel containing the back button
   */
  private JPanel createBackButtonPanel() {
    JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton backButton = new JButton("Back");
    backButton.setPreferredSize(
      new Dimension(App.WIDTH - 20, backButton.getPreferredSize().height)
    );
    backButtonPanel.add(backButton);

    backButton.addActionListener(e -> {
      removeAll();
      add(createHeaderPanel("Explore 🐥"), BorderLayout.NORTH);
      add(createMainContentPanel(), BorderLayout.CENTER);
      revalidate();
      repaint();
    });

    return backButtonPanel;
  }

  // Assuming ImageDetails is a class that encapsulates image details.
  // Replace this with the actual class or structure you're using.
  private class ImageDetails {

    private String username;
    private String bio;
    private String timestampString;
    private int likes;

    /**
     * Constructs an ImageDetails object with the provided details.
     *
     * @param details the string containing the image details in the format:
     *                "id: [imageId], username: [username], bio: [bio], timestamp: [timestamp], likes: [likes]"
     * @throws IllegalArgumentException if the provided details string does not have the correct format
     */
    public ImageDetails(String details) {
      String[] parts = details.split(", (?![^\\[]*\\])");
      if (parts.length != 5) {
        System.out.println("Invalid image details format: " + details);
      }
      username = parts[1].split(": ")[1];
      bio = parts[2].split(": ")[1];
      timestampString = parts[3].split(": ")[1];

      String likesString = parts[4].split(": ")[1];
      likesString = likesString.substring(1, likesString.length() - 1); // Remove the brackets
      likes = likesString.isEmpty() ? 0 : likesString.split(", ").length;
    }

    // Getters
    public String getUsername() {
      return username;
    }

    public String getBio() {
      return bio;
    }

    public String getTimestampString() {
      return timestampString;
    }

    public int getLikes() {
      return likes;
    }

    /**
     * Toggles the like status of an image for a given user.
     * If the user has already liked the image, it removes the like.
     * If the user has not liked the image, it adds the like.
     *
     * @param imageId The ID of the image to toggle the like for.
     * @param username The username of the user toggling the like.
     */
    public void toggleLike(String imageId, String username) {
      System.out.println("Liked the picture!");
      // Update the details file with the new likes count
      try {
        Path detailsPath = Paths.get(AppPaths.IMAGE_DETAILS);
        List<String> lines = Files.readAllLines(detailsPath);
        List<String> updatedLines = new ArrayList<>();
        for (String line : lines) {
          if (line.contains("ImageID: " + imageId)) {
            String[] parts = line.split(", (?![^\\[]*\\])");
            String likes = parts[4].split(": ")[1];
            likes =
              likes.equals("[]") ? "" : likes.substring(1, likes.length() - 1); // Remove the brackets
            // Check if the current user's username is already in the likes string
            if (likes.contains(username)) {
              System.out.println(
                "User has already liked this image, unliking it"
              );
              likes =
                likes
                  .replace(username + ", ", "")
                  .replace(", " + username, "")
                  .replace(username, "");
            } else {
              likes = likes.isEmpty() ? username : likes + ", " + username; // Add the new username
            }
            parts[4] = "Likes: [" + likes + "]";
            line = String.join(", ", parts);
            this.likes = likes.isEmpty() ? 0 : likes.split(", ").length;
          }
          updatedLines.add(line);
        }
        Files.write(detailsPath, updatedLines);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
}
