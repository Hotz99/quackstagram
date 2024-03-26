package image;

import app.App;
import auth.UserManager;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

public class ImageViewer {

  private boolean goHome = false;

  private final AppPathsSingleton appPathsSingleton = AppPathsSingleton.getInstance();
  private final String imageDetails = appPathsSingleton.IMAGE_DETAILS;

  /**
   * Displays the image with the given image path.
   *
   * @param imagePath the path of the image to be displayed
   */
  public void displayImage(String headerLabel, String imagePath) {
    goHome = headerLabel.toLowerCase().contains("home");

    String imageId = extractImageId(imagePath);
    ImageDetails imageDetails = readImageDetails(imageId);

    if (imageDetails == null) {
      System.out.println("No image details found for ImageID: " + imageId);
      return;
    }

    App.imageView.removeAll();
    App.imageView.setLayout(new BorderLayout());
    App.imageView.add(
      App.imageView.createHeaderPanel(headerLabel),
      BorderLayout.NORTH
    );

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

    App.imageView.add(containerPanel, BorderLayout.CENTER);
    App.imageView.add(createBackButtonPanel(), BorderLayout.SOUTH);

    // make the image likeable
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

    App.imageView.revalidate();
    App.imageView.repaint();

    app.App.showPanel("Image View");
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
  public ImageDetails readImageDetails(String imageId) {
    Path detailsPath = Paths.get(imageDetails);
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
   * @param timestampString the timestamp of the post in the format "yyyy-MM-dd
   *                        HH:mm:ss"
   * @return a string representing the time since the post was made, e.g. "2 days
   *         ago"
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
   * @param username         the username of the user
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
      System.out.println("Clicked on username: " + username);
      App.showPanelWithUsername(username);
    });

    return topPanel;
  }

  /**
   * Creates and returns a JLabel with the specified image.
   *
   * @param imagePath the path to the image file
   * @return a JLabel with the specified image, or a label with "Image not found"
   *         text if the image file is not found
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

    if (goHome) {
      backButton.addActionListener(e -> App.showPanel("Home"));
    } else {
      backButton.addActionListener(e -> App.showPanel("Explore"));
    }

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
     *                "id: [imageId], username: [username], bio: [bio], timestamp:
     *                [timestamp], likes: [likes]"
     * @throws IllegalArgumentException if the provided details string does not have
     *                                  the correct format
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
     * @param imageId  The ID of the image to toggle the like for.
     * @param username The username of the user toggling the like.
     */
    public void toggleLike(String imageId, String username) {
      System.out.println("Liked the picture!");
      // Update the details file with the new likes count
      try {
        Path detailsPath = Paths.get(imageDetails);
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
