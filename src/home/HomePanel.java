package home;

import app.App;
import auth.UserManager;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import utils.AppPathsSingleton;
import utils.BasePanel;

public class HomePanel extends BasePanel {

  // Constants for the panel
  private static final int IMAGE_WIDTH = App.WIDTH - 100; // Width for the image posts
  private static final int IMAGE_HEIGHT = 150; // Height for the image posts
  private static final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for the like button

  // Singleton instances
  private final AppPathsSingleton appPathsSingleton = AppPathsSingleton.getInstance();
  private final UserManager userManager = UserManager.getInstance();

  // Paths and file names
  private final String imageDetails = appPathsSingleton.IMAGE_DETAILS;
  private final String following = appPathsSingleton.FOLLOWING;
  private final String uploaded = appPathsSingleton.UPLOADED;

  // Map to store likes for each image
  private Map<String, Integer> likes = new HashMap<>();

  // UI components
  private CardLayout cardLayout;
  private JPanel cardPanel;
  private JPanel imageViewPanel;

  public HomePanel() {
    super(false, false, false);
    // Create header panel and add it to the north
    add(createHeaderPanel("Quackstagram Home"), BorderLayout.NORTH);

    // Create content panel and populate it with sample data
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    String[][] sampleData = createSampleData();
    populateContentPanel(this, contentPanel, sampleData);

    // Create home panel and add it to the card panel
    JScrollPane homePanel = new JScrollPane(contentPanel);
    homePanel.setHorizontalScrollBarPolicy(
      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
    );
    imageViewPanel = new JPanel(new BorderLayout());
    cardPanel = new JPanel(cardLayout = new CardLayout());
    cardPanel.add(homePanel, "Home");
    cardPanel.add(imageViewPanel, "ImageView");

    // Add card panel to the center
    add(cardPanel, BorderLayout.CENTER);
  }

  private JPanel createItemPanel() {
    JPanel itemPanel = new JPanel();
    itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
    itemPanel.setBackground(Color.WHITE);
    itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    itemPanel.setAlignmentX(CENTER_ALIGNMENT);
    return itemPanel;
  }

  private void configureImageLabel(JLabel imageLabel, String imagePath) {
    imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
    imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    ImageIcon imageIcon = loadImageIcon(imagePath);
    if (imageIcon != null) {
      imageLabel.setIcon(imageIcon);
    } else {
      imageLabel.setText("Image not found");
    }
  }

  private JButton createLikeButton(
    String imageId,
    JLabel likesLabel,
    JPanel itemPanel
  ) {
    JButton likeButton = new JButton("❤");
    likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    likeButton.setBackground(LIKE_BUTTON_COLOR);
    likeButton.setOpaque(true);
    likeButton.setBorderPainted(false);
    likeButton.addActionListener(e -> {
      toggleLike(imageId, userManager.getCurrentUser().getUsername());
      likesLabel.setText(String.valueOf(likes.get(imageId)));
      itemPanel.revalidate();
      itemPanel.repaint();
    });
    return likeButton;
  }

  private void populateContentPanel(
    BasePanel homePanel,
    JPanel panel,
    String[][] sampleData
  ) {
    for (String[] postData : sampleData) {
      JPanel itemPanel = createItemPanel();
      JLabel nameLabel = new JLabel(postData[0]);
      nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

      JLabel imageLabel = new JLabel();
      configureImageLabel(imageLabel, postData[3]);

      JLabel descriptionLabel = new JLabel(postData[1]);
      descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

      int likesCount =
        postData[2].length() - postData[2].replace(",", "").length() + 1;

      likesCount = postData[2].equals("[]") ? 0 : likesCount;

      JLabel likesLabel = new JLabel(String.valueOf(likesCount));
      likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

      JButton likeButton = createLikeButton(
        new File(postData[3]).getName().split("\\.")[0],
        likesLabel,
        itemPanel
      );

      itemPanel.add(nameLabel);
      itemPanel.add(imageLabel);
      itemPanel.add(descriptionLabel);
      itemPanel.add(likesLabel);
      itemPanel.add(likeButton);

      panel.add(itemPanel);

      imageLabel.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            App.imageViewer.displayImage(" Quackstagram Home ", postData[3]);
          }
        }
      );

      addSpacingPanel(panel);
    }
  }

  private void addSpacingPanel(JPanel panel) {
    JPanel spacingPanel = new JPanel();
    spacingPanel.setPreferredSize(new Dimension(App.WIDTH - 10, 5));
    spacingPanel.setBackground(new Color(230, 230, 230));
    panel.add(spacingPanel);
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
          this.likes.put(
              imageId,
              likes.isEmpty() ? 0 : likes.split(", ").length
            );
        }
        updatedLines.add(line);
      }
      Files.write(detailsPath, updatedLines);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private String readFollowedUsers(String currentUser) {
    try (
      BufferedReader reader = Files.newBufferedReader(Paths.get(following))
    ) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith(currentUser + ":")) {
          return line.split(":")[1].trim();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  private String[][] readImageDetails(String followedUsers) {
    List<String[]> tempList = new ArrayList<>();
    try (
      BufferedReader reader = Files.newBufferedReader(Paths.get(imageDetails))
    ) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] details = line.split(", ");
        String imagePoster = ""; // Default to empty or a placeholder value
        String[] splitDetail = details[1].split(": ");
        if (splitDetail.length > 1) {
          imagePoster = splitDetail[1]; // Only access the element if it exists
        }
        if (followedUsers.contains(imagePoster)) {
          String imagePath = uploaded + details[0].split(": ")[1] + ".png";
          String description = details[2].split(": ")[1];
          String likes = "Likes: " + details[4].split(": ")[1];
          tempList.add(
            new String[] { imagePoster, description, likes, imagePath }
          );
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return tempList.toArray(new String[0][]);
  }

  private String[][] createSampleData() {
    String currentUser = userManager.getCurrentUser().getUsername();
    String followedUsers = readFollowedUsers(currentUser);
    return readImageDetails(followedUsers);
  }

  private ImageIcon loadImageIcon(String imagePath) {
    try {
      BufferedImage originalImage = ImageIO.read(new File(imagePath));
      Image scaledImage = originalImage.getScaledInstance(
        App.WIDTH - 40,
        App.HEIGHT - 20,
        Image.SCALE_SMOOTH
      );
      return new ImageIcon(scaledImage);
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public int countLikes(String imageId) {
    try {
      System.out.println("Counting likes for image: " + imageId);
      Path detailsPath = Paths.get(imageDetails);
      List<String> lines = Files.readAllLines(detailsPath);
      for (String line : lines) {
        if (line.contains("ImageID: " + imageId)) {
          // Extract the likes part
          String[] parts = line.split(", (?![^\\[]*\\])");
          String likes = parts[4].split(": ")[1];
          likes =
            likes.equals("[]") ? "" : likes.substring(1, likes.length() - 1); // Remove the brackets

          if (!likes.isEmpty()) {
            // Count the number of likes by splitting the likes string by ", "
            final int likeCount = likes.split(", ").length;
            System.out.println("Counted likes: " + likeCount);
            this.likes.put(imageId, likeCount);
            return likes.split(", ").length;
          }
          break; // Stop searching once the relevant line is found
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    this.likes.put(imageId, 0);
    System.out.println("Counted likes: " + 0);
    return 0; // Return 0 if there are no likes or in case of an error
  }
}
