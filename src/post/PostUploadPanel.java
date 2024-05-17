package post;

import app.App;
import auth.UserManager;
import database.models.Post;
import database.models.User;
import database.repositories.PostRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import utils.AppPathsSingleton;
import utils.BasePanel;
import utils.HeaderFactory;

public class PostUploadPanel extends BasePanel {

  private JLabel imagePreviewLabel;
  private JTextArea captionTextArea;
  private JButton uploadButton;

  private final String uploaded = appPaths.UPLOADED;

  public PostUploadPanel() {
    super(false, false, false);
    JPanel headerPanel = HeaderFactory.createHeader(" Upload Image 🐥");

    // Main content panel
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    // Image preview
    imagePreviewLabel = createImagePreviewLabel();

    // Set an initial empty icon to the imagePreviewLabel
    ImageIcon emptyImageIcon = new ImageIcon();
    imagePreviewLabel.setIcon(emptyImageIcon);

    contentPanel.add(imagePreviewLabel);

    // Bio text area
    contentPanel.add(createBioTextArea());

    // Upload button
    contentPanel.add(createUploadButton());

    // Add panels to frame
    add(headerPanel, BorderLayout.NORTH);
    add(contentPanel, BorderLayout.CENTER);
  }

  private JLabel createImagePreviewLabel() {
    JLabel imagePreviewLabel = new JLabel();
    imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    imagePreviewLabel.setPreferredSize(
        new Dimension(App.getAppWidth(), App.getAppHeight() / 3));
    return imagePreviewLabel;
  }

  private JScrollPane createBioTextArea() {
    captionTextArea = new JTextArea("Enter a caption ...");
    captionTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
    captionTextArea.setLineWrap(true);
    captionTextArea.setWrapStyleWord(true);
    JScrollPane bioScrollPane = new JScrollPane(captionTextArea);
    bioScrollPane.setPreferredSize(
        new Dimension(App.getAppWidth() - 50, App.getAppHeight() / 6));
    return bioScrollPane;
  }

  private JButton createUploadButton() {
    uploadButton = new JButton("Upload Image");
    uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    uploadButton.addActionListener(this::uploadAction);
    return uploadButton;
  }

  private void uploadAction(ActionEvent event) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select an image file");
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.addChoosableFileFilter(
        new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg"));

    int returnValue = fileChooser.showOpenDialog(null);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();

      try {
        User user = userManager.getCurrentUser();

        // lil sql injection opportunity here wink wink
        Post newPost = postRepo.savePost(user, this.captionTextArea.getText(),
            getFileExtension(selectedFile));

        Path uploadedImagePath = Paths.get(uploaded + newPost.getImagePath());

        // move uploaded image to 'uploads' directory
        Path finalUploadPath = Files.copy(
            selectedFile.toPath(),
            uploadedImagePath,
            StandardCopyOption.REPLACE_EXISTING);

        System.out.println(
            "Image uploaded to: " + finalUploadPath.toString());

        // Load the image from the saved path
        ImageIcon imageIcon = new ImageIcon(uploadedImagePath.toString());

        // Check if imagePreviewLabel has a valid size
        if (imagePreviewLabel.getWidth() <= 0 ||
            imagePreviewLabel.getHeight() <= 0)
          return;

        // Set the image icon with the scaled image
        imageIcon.setImage(
            getScaledImage(imageIcon.getImage(), imagePreviewLabel));

        imagePreviewLabel.setIcon(imageIcon);

        // Change the text of the upload button
        uploadButton.setText("Upload Another Image");

        JOptionPane.showMessageDialog(
            this,
            "Image uploaded and preview updated!");
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(
            this,
            "Error saving image: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private Image getScaledImage(Image image, JLabel imagePreviewLabel) {
    int previewWidth = imagePreviewLabel.getWidth();
    int previewHeight = imagePreviewLabel.getHeight();
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    double widthRatio = (double) previewWidth / imageWidth;
    double heightRatio = (double) previewHeight / imageHeight;
    double scale = Math.min(widthRatio, heightRatio);
    int scaledWidth = (int) (scale * imageWidth);
    int scaledHeight = (int) (scale * imageHeight);

    Image scaledImage = image.getScaledInstance(
        scaledWidth,
        scaledHeight,
        Image.SCALE_SMOOTH);

    return scaledImage;
  }

  private String getFileExtension(File file) {
    String fileName = file.getName();
    return fileName.substring(fileName.lastIndexOf(".") + 1);
  }
}
