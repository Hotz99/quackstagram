package post;

import app.App;
import auth.UserManager;
import database.models.Post;
import database.models.User;
import database.repositories.LikeRepository;
import database.repositories.PostRepository;
import database.repositories.UserRepository;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import utils.*;

public class PostImageViewer {

  private boolean goHome = false;

  private final AppPathsSingleton appPaths = AppPathsSingleton.getInstance();
  private final UserManager userManager = UserManager.getInstance();
  private final LikeRepository likeRepo = LikeRepository.getInstance();

  /**
   * Displays the image with the given image path.
   *
   * @param imagePath the path of the image to be displayed
   */
  public void displayImage(String headerLabel, String imagePath) {
    goHome = headerLabel.toLowerCase().contains("home");

    // imagePath has format
    // "resources/images/uploaded/[postIdx]_[user_id].[file_extension]"
    String fileName = imagePath.substring(imagePath.lastIndexOf('/') + 1, imagePath.length());

    // fileName = [postIdx]_[user_id].[file_extension] is a unique identifier
    Post post = PostRepository.getInstance().getByFileName(fileName);
    User postUser = UserRepository.getInstance().getById(post.getUserId());

    App.imageView.removeAll();
    App.imageView.setLayout(new BorderLayout());
    App.imageView.add(
        HeaderFactory.createHeader(headerLabel),
        BorderLayout.NORTH);

    JPanel topPanel = createTopPanel(
        postUser.getUsername(),
        post.getPostedDate().toString());
    JLabel imageLabel = prepareImageLabel(post.getImagePath());

    JPanel bottomPanel = createBottomPanel(
        post.getCaption(),
        post.getLikesCount());

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

              likeRepo.toggleLike(post.getPostId(),
                  userManager.getCurrentUser().getUserId());

              ((JLabel) bottomPanel.getComponent(1))
                  .setText("Likes: " + likeRepo.getLikesCountByPostId(post.getPostId()));
            }
          }
        });

    App.imageView.revalidate();
    App.imageView.repaint();

    app.App.showPanel("Image View");
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
      App.showProfileByUsername(username);
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
      BufferedImage originalImage = ImageIO.read(new File(appPaths.UPLOADED + imagePath));
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
        new Dimension(App.WIDTH - 20, backButton.getPreferredSize().height));
    backButtonPanel.add(backButton);

    if (goHome) {
      backButton.addActionListener(e -> App.showPanel("Home"));
    } else {
      backButton.addActionListener(e -> App.showPanel("Explore"));
    }

    return backButtonPanel;
  }
}
