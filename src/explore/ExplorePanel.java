package explore;

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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    JTextField searchField = new JTextField(" Search");
    searchField.addFocusListener(
      new FocusListener() {
        public void focusGained(FocusEvent e) {
          if (searchField.getText().equals(" Search")) {
            searchField.setText("");
          }
        }

        public void focusLost(FocusEvent e) {
          if (searchField.getText().isEmpty()) {
            searchField.setText(" Search");
          }
        }
      }
    );

    searchField
      .getDocument()
      .addDocumentListener(
        new DocumentListener() {
          public void changedUpdate(DocumentEvent e) {
            runSearch();
          }

          public void removeUpdate(DocumentEvent e) {
            runSearch();
          }

          public void insertUpdate(DocumentEvent e) {
            runSearch();
          }

          public void runSearch() {
            String query = searchField.getText();
            if (query.trim().isEmpty()) {
              System.out.println("Search field is empty, not running search");
              return;
            }
            List<String> results = Search.search(query);
            System.out.println("found this after searching: " + results);
          }
        }
      );

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
}
