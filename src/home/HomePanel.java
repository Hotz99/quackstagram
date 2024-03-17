package home;

import java.util.List;
import java.util.ArrayList;
import app.App;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.*;
import utils.AppPaths;
import utils.BasePanel;

public class HomePanel extends BasePanel {

  private static final int IMAGE_WIDTH = App.WIDTH - 100; // Width for the image posts
  private static final int IMAGE_HEIGHT = 150; // Height for the image posts
  private static final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for the like button

  private CardLayout cardLayout;
  private JPanel cardPanel;
  private JPanel homePanel;
  private JPanel imageViewPanel;

  public HomePanel() {
    add(createHeaderPanel("Quackstagram Home"), BorderLayout.NORTH);

    // Content Scroll Panel
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout

    String[][] sampleData = createSampleData();
    populateContentPanel(contentPanel, sampleData);

    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setHorizontalScrollBarPolicy(
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    // Set up the home panel
    homePanel = new JPanel(new BorderLayout());
    homePanel.add(scrollPane, BorderLayout.CENTER);

    imageViewPanel = new JPanel(new BorderLayout());

    cardPanel = new JPanel(cardLayout = new CardLayout());
    cardPanel.add(homePanel, "Home");
    cardPanel.add(imageViewPanel, "ImageView");

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

  private JButton createLikeButton(String imageId, JLabel likesLabel) {
    JButton likeButton = new JButton("❤");
    likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    likeButton.setBackground(LIKE_BUTTON_COLOR);
    likeButton.setOpaque(true);
    likeButton.setBorderPainted(false);
    likeButton.addActionListener(e -> handleLikeAction(imageId, likesLabel));
    return likeButton;
  }

  private void populateContentPanel(JPanel panel, String[][] sampleData) {
    for (String[] postData : sampleData) {
      JPanel itemPanel = createItemPanel();
      JLabel nameLabel = new JLabel(postData[0]);
      nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

      JLabel imageLabel = new JLabel();
      configureImageLabel(imageLabel, postData[3]);

      JLabel descriptionLabel = new JLabel(postData[1]);
      descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

      int likesCount = postData[2].length() - postData[2].replace(",", "").length() + 1;

      likesCount = postData[2].equals("[]") ? 0 : likesCount;

      JLabel likesLabel = new JLabel(
          String.valueOf(likesCount));
      likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

      JButton likeButton = createLikeButton(new File(postData[3]).getName().split("\\.")[0], likesLabel);

      itemPanel.add(nameLabel);
      itemPanel.add(imageLabel);
      itemPanel.add(descriptionLabel);
      itemPanel.add(likesLabel);
      itemPanel.add(likeButton);

      panel.add(itemPanel);

      imageLabel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          displayImage(postData);
        }
      });

      addSpacingPanel(panel);
    }
  }

  private void addSpacingPanel(JPanel panel) {
    JPanel spacingPanel = new JPanel();
    spacingPanel.setPreferredSize(new Dimension(App.WIDTH - 10, 5));
    spacingPanel.setBackground(new Color(230, 230, 230));
    panel.add(spacingPanel);
  }

  // TODO
  private void handleLikeAction(String imageId, JLabel likesLabel) {
    Path detailsPath = Paths.get(AppPaths.IMAGE_DETAILS);
    StringBuilder newContent = new StringBuilder();
    boolean updated = false;
    String currentUser = "";
    String imageOwner = "";
    String timestamp = LocalDateTime
        .now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // Retrieve the current user from users.txt
    try (
        BufferedReader userReader = Files.newBufferedReader(
            Paths.get(AppPaths.USERS))) {
      String line = userReader.readLine();
      if (line != null) {
        currentUser = line.split(":")[0].trim();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Read and update image_details.txt
    try (BufferedReader reader = Files.newBufferedReader(detailsPath)) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains("ImageID: " + imageId)) {
          String[] parts = line.split(", ");
          imageOwner = parts[1].split(": ")[1];

          // Test integration
          int likes = 0;
          try {
            likes = Integer.parseInt(parts[4].split(": ")[1].trim());
          } catch (NumberFormatException e) {
            System.err.println(
                "Error parsing likes count for image ID (file HomePanel line 189)" + imageId + ": " + e.getMessage());
            continue; // Skip this record or handle as appropriate
          }

          // int likes = Integer.parseInt(parts[4].split(": ")[1]);
          likes++; // Increment the likes count
          parts[4] = "Likes: " + likes;
          line = String.join(", ", parts);

          // Update the UI
          likesLabel.setText("Likes: " + likes);
          updated = true;
        }
        newContent.append(line).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (!updated)
      return;

    // Write updated likes back to image_details.txt
    try (BufferedWriter writer = Files.newBufferedWriter(detailsPath)) {
      writer.write(newContent.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Record the like in notifications.txt
    String notification = String.format(
        "%s; %s; %s; %s\n",
        imageOwner,
        currentUser,
        imageId,
        timestamp);
    try (
        BufferedWriter notificationWriter = Files.newBufferedWriter(
            Paths.get(AppPaths.NOTIFICATIONS),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND)) {
      notificationWriter.write(notification);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String readCurrentUser() {
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(AppPaths.USERS))) {
      String line = reader.readLine();
      if (line != null) {
        return line.split(":")[0].trim();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  private String readFollowedUsers(String currentUser) {
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(AppPaths.FOLLOWING))) {
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
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(AppPaths.IMAGE_DETAILS))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] details = line.split(", ");
        String imagePoster = ""; // Default to empty or a placeholder value
        String[] splitDetail = details[1].split(": ");
        if (splitDetail.length > 1) {
          imagePoster = splitDetail[1]; // Only access the element if it exists
        }
        if (followedUsers.contains(imagePoster)) {
          String imagePath = AppPaths.UPLOADED + details[0].split(": ")[1] + ".png";
          String description = details[2].split(": ")[1];
          String likes = "Likes: " + details[4].split(": ")[1];
          tempList.add(new String[] { imagePoster, description, likes, imagePath });
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return tempList.toArray(new String[0][]);
  }

  private String[][] createSampleData() {
    String currentUser = readCurrentUser();
    String followedUsers = readFollowedUsers(currentUser);
    return readImageDetails(followedUsers);
  }

  private ImageIcon loadImageIcon(String imagePath) {
    try {
      BufferedImage originalImage = ImageIO.read(new File(imagePath));
      Image scaledImage = originalImage.getScaledInstance(App.WIDTH - 40, App.HEIGHT - 20, Image.SCALE_SMOOTH);
      return new ImageIcon(scaledImage);
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  private JPanel createUserPanel(String userNameText) {
    JPanel userPanel = new JPanel();
    userPanel.setLayout(new BorderLayout());

    JLabel userName = new JLabel(userNameText);
    userName.setFont(new Font("Arial", Font.BOLD, 18));
    userPanel.add(userName, BorderLayout.WEST);

    JButton returnButton = new JButton("Return");
    returnButton.addActionListener(e -> cardLayout.show(cardPanel, "Home"));
    userPanel.add(returnButton, BorderLayout.EAST);

    return userPanel;
  }

  private JButton createLikeButton(String imageId, JLabel likesLabel, String[] postData) {
    JButton likeButton = new JButton("❤");
    likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    likeButton.setBackground(LIKE_BUTTON_COLOR);
    likeButton.setOpaque(true);
    likeButton.setBorderPainted(false);
    likeButton.addActionListener(e -> {
      handleLikeAction(imageId, likesLabel);
      refreshDisplayImage(postData, imageId);
    });
    return likeButton;
  }

  private JPanel createInfoPanel(String description, JLabel likesLabel, JButton likeButton) {
    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.add(new JLabel(description));
    infoPanel.add(likesLabel);
    infoPanel.add(likeButton);
    return infoPanel;
  }

  private void displayImage(String[] postData) {
    imageViewPanel.removeAll();
    String imageId = new File(postData[3]).getName().split("\\.")[0];
    JLabel likesLabel = new JLabel("Likes: " + postData[2]);
    System.out.println("imgicon: " + postData[3]);
    ImageIcon imageIcon = loadImageIcon(postData[3]);

    JLabel fullSizeImageLabel = new JLabel();
    fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
    if (imageIcon != null) {
      fullSizeImageLabel.setIcon(imageIcon);
    } else {
      fullSizeImageLabel.setText("Image not found");
    }

    JPanel userPanel = createUserPanel(postData[0]);
    JButton likeButton = createLikeButton(imageId, likesLabel, postData);
    JPanel infoPanel = createInfoPanel(postData[1], likesLabel, likeButton);

    imageViewPanel.add(fullSizeImageLabel, BorderLayout.CENTER);
    imageViewPanel.add(infoPanel, BorderLayout.SOUTH);
    imageViewPanel.add(userPanel, BorderLayout.NORTH);

    imageViewPanel.revalidate();
    imageViewPanel.repaint();
    cardLayout.show(cardPanel, "ImageView");
  }

  private void refreshDisplayImage(String[] postData, String imageId) {
    // Read updated likes count from image_details.txt
    try (
        BufferedReader reader = Files.newBufferedReader(
            Paths.get(AppPaths.IMAGE_DETAILS))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains("ImageID: " + imageId)) {
          String likes = line.split(", ")[4].split(": ")[1];
          postData[2] = "Likes: " + likes;
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Call displayImage with updated postData
    displayImage(postData);
  }
}
