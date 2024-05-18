package post;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import app.App;
import database.models.Post;
import utils.BasePanel;
import utils.HeaderFactory;

public class PostUploadPanel extends BasePanel {
  private JTextArea captionTextArea;
  private JButton uploadButton;

  private final String uploaded = appPaths.UPLOADED;

  public PostUploadPanel() {
    super(false, false, false);
    JPanel headerPanel = HeaderFactory.createHeader(" Upload Image 🐥");

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    contentPanel.add(createCaptionTextArea());

    // Upload button
    contentPanel.add(createUploadButton());

    // Add panels to frame
    add(headerPanel, BorderLayout.NORTH);
    add(contentPanel, BorderLayout.CENTER);
  }

  private JScrollPane createCaptionTextArea() {
    captionTextArea = new JTextArea();
    captionTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
    captionTextArea.setLineWrap(true);
    captionTextArea.setWrapStyleWord(true);

    captionTextArea.setText("Enter a caption for your post...");
    captionTextArea.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        if (captionTextArea.getText().equals("Enter a caption for your post...")) {
          captionTextArea.setText("");
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        if (captionTextArea.getText().isEmpty()) {
          captionTextArea.setText("Enter a caption for your post...");
        }
      }
    });

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
        // lil sql injection opportunity here
        Post newPost = postRepo.savePost(userManager.getCurrentUser(), this.captionTextArea.getText(),
            getFileExtension(selectedFile));

        Path uploadedImagePath = Paths.get(uploaded + newPost.getImagePath());

        // move uploaded image to 'uploads' directory
        Path finalUploadPath = Files.copy(
            selectedFile.toPath(),
            uploadedImagePath,
            StandardCopyOption.REPLACE_EXISTING);

        System.out.println(
            "image uploaded to: " + finalUploadPath.toString());

        uploadButton.setText("Upload Another Image");

        JOptionPane.showMessageDialog(
            this,
            "Image uploaded successfully.");
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(
            this,
            "Error saving image: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private String getFileExtension(File file) {
    String fileName = file.getName();
    return fileName.substring(fileName.lastIndexOf(".") + 1);
  }
}
