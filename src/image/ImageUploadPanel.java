package image;

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

import app.App;
import utils.AppPaths;
import utils.BasePanel;

public class ImageUploadPanel extends BasePanel {
  private JLabel imagePreviewLabel;
  private JTextArea bioTextArea;
  private JButton uploadButton;
  private JButton saveButton;

  public ImageUploadPanel() {
    super(false, false, false);
    JPanel headerPanel = createHeaderPanel(" Upload Image 🐥"); // Reuse the createHeaderPanel method

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

    // Save button (for bio)
    contentPanel.add(createSaveButton());

    // Add panels to frame
    add(headerPanel, BorderLayout.NORTH);
    add(contentPanel, BorderLayout.CENTER);
  }

  private JLabel createImagePreviewLabel() {
    JLabel imagePreviewLabel = new JLabel();
    imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    imagePreviewLabel.setPreferredSize(new Dimension(App.WIDTH, App.HEIGHT / 3));
    return imagePreviewLabel;
  }

  private JScrollPane createBioTextArea() {
    bioTextArea = new JTextArea("Enter a caption");
    bioTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
    bioTextArea.setLineWrap(true);
    bioTextArea.setWrapStyleWord(true);
    JScrollPane bioScrollPane = new JScrollPane(bioTextArea);
    bioScrollPane.setPreferredSize(new Dimension(App.WIDTH - 50, App.HEIGHT / 6));
    return bioScrollPane;
  }

  private JButton createUploadButton() {
    uploadButton = new JButton("Upload Image");
    uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    uploadButton.addActionListener(this::uploadAction);
    return uploadButton;
  }

  private JButton createSaveButton() {
    saveButton = new JButton("Save Caption");
    saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    saveButton.addActionListener(this::saveBioAction);
    return saveButton;
  }

  private void uploadAction(ActionEvent event) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select an image file");
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
        "Image files",
        "png",
        "jpg",
        "jpeg"));

    int returnValue = fileChooser.showOpenDialog(null);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();

      try {
        String username = getCurrentUsername(); // Read username from users.txt
        int nextImageId = getNextImageId(username);
        String fileExtension = getFileExtension(selectedFile);
        String newFileName = username + "_" + nextImageId + "." + fileExtension;

        Path uploadedImagePath = Paths.get(
            AppPaths.UPLOADED +
                newFileName);
        Files.copy(
            selectedFile.toPath(),
            uploadedImagePath,
            StandardCopyOption.REPLACE_EXISTING);

        // Save the bio and image ID to a text file
        saveImageDetails(
            username + "_" + nextImageId,
            username,
            bioTextArea.getText());

        System.out.println("Image uploaded to: " + uploadedImagePath.toString());

        // Load the image from the saved path
        ImageIcon imageIcon = new ImageIcon(uploadedImagePath.toString());

        // Check if imagePreviewLabel has a valid size
        if (imagePreviewLabel.getWidth() <= 0 || imagePreviewLabel.getHeight() <= 0)
          return;

        // Set the image icon with the scaled image
        imageIcon.setImage(getScaledImage(imageIcon.getImage(), imagePreviewLabel));

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

  private int getNextImageId(String username) throws IOException {
    Path storageDir = Paths.get(
        AppPaths.UPLOADED); // Ensure this is the directory where images are saved
    if (!Files.exists(storageDir)) {
      Files.createDirectories(storageDir);
    }

    int maxId = 0;

    try (
        DirectoryStream<Path> stream = Files.newDirectoryStream(
            storageDir,
            username + "_*")) {
      for (Path path : stream) {
        String fileName = path.getFileName().toString();
        int idEndIndex = fileName.lastIndexOf('.');
        if (idEndIndex != -1) {
          String idStr = fileName.substring(username.length() + 1, idEndIndex);
          try {
            int id = Integer.parseInt(idStr);
            if (id > maxId) {
              maxId = id;
            }
          } catch (NumberFormatException ex) {
            // Ignore filenames that do not have a valid numeric ID
            System.out.println("Invalid image filename: " + fileName);
          }
        }
      }
    }
    return maxId + 1; // Return the next available ID
  }

  private void saveImageDetails(String imageId, String username, String bio)
      throws IOException {
    Path imageDetailsPath = Paths.get(AppPaths.IMAGE_DETAILS);
    if (!Files.exists(imageDetailsPath)) {
      Files.createFile(imageDetailsPath);
    }

    String timestamp = LocalDateTime
        .now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    try (
        BufferedWriter writer = Files.newBufferedWriter(
            imageDetailsPath,
            StandardOpenOption.APPEND)) {
      writer.write(
          String.format(
              "ImageID: %s, Username: %s, Bio: %s, Timestamp: %s, Likes: 0",
              imageId,
              username,
              bio,
              timestamp));
      writer.newLine();
    }
  }

  private String getFileExtension(File file) {
    String fileName = file.getName();
    return fileName.substring(fileName.lastIndexOf(".") + 1);
  }

  private void saveBioAction(ActionEvent event) {
    // Here you would handle saving the bio text
    String bioText = bioTextArea.getText();
    // For example, save the bio text to a file or database
    JOptionPane.showMessageDialog(this, "Caption saved: " + bioText);
  }

  private String getCurrentUsername() throws IOException {
    Path usersFilePath = Paths.get(AppPaths.USERS);
    try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
      String line = reader.readLine();
      if (line != null) {
        System.out.println("Read username: " + line.split(":")[0]);
        return line.split(":")[0]; // Extract the username from the first line
      }
    }
    return null; // Return null if no username is found
  }
}
