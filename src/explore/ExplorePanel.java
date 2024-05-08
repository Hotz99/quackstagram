package explore;

import app.App;
import auth.UserManager;
import post.PostImageViewer;

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
 * The ExplorePanel class represents a panel that displays the explore
 * functionality of the application.
 * It extends the BasePanel class and provides methods to create the main
 * content panel, search panel, image grid panel,
 * and other UI components related to exploring and displaying images.
 */
public class ExplorePanel extends BasePanel {

  private final int IMAGE_SIZE = App.WIDTH / 3; // Size for each image in the grid

  // singleton pattern
  private final AppPathsSingleton appPathsSingleton = AppPathsSingleton.getInstance();
  private final String uploaded = appPathsSingleton.UPLOADED;
  private static ExplorePanel instance = null;
  private SearchResult searchResult; // Instance variable for SearchResult

  /**
   * The ExplorePanel class represents a panel that displays the explore
   * functionality in the application.
   * It contains a header panel and a main content panel.
   */
  private ExplorePanel() {
    super(false, false, false);
    removeAll(); // Clear existing components
    setLayout(new BorderLayout()); // Reset the layout manager

    JPanel headerPanel = HeaderFactory.createHeader("Explore 🐥");
    JPanel mainContentPanel = createMainContentPanel(this);

    // Add panels to the frame
    add(headerPanel, BorderLayout.NORTH);
    add(mainContentPanel, BorderLayout.CENTER);

    revalidate();
    repaint();
  }

  // Public static method to get the single instance of ExplorePanel
  public static ExplorePanel getInstance() {
    if (instance == null) {
      instance = new ExplorePanel();
    }
    return instance;
  }

  public void overlayComponent(Component component) {
    JLayeredPane layeredPane = this.getRootPane().getLayeredPane();

    // Set the size and position of the component to take up the bottom 50% of the
    // screen
    int height = this.getHeight();
    int width = this.getWidth();
    component.setBounds(0, height / 2, width, height / 2);

    // Add the component to the layered pane at the highest z-index
    layeredPane.add(component, JLayeredPane.POPUP_LAYER);
  }

  public void closeOverlayComponents() {
    try {
      JLayeredPane layeredPane = this.getRootPane().getLayeredPane();
      // Remove all components in the POPUP_LAYER
      for (Component comp : layeredPane.getComponentsInLayer(
          JLayeredPane.POPUP_LAYER)) {
        layeredPane.remove(comp);
      }

      // Repaint the layered pane to reflect the changes
      layeredPane.repaint();
    } catch (Exception e) {
      System.out.println("failed to close explore panel");
      e.printStackTrace();
    }
  }

  /**
   * Creates and returns a JPanel that serves as the main content panel.
   * The main content panel contains a search panel and an image grid panel
   * wrapped in a scroll pane.
   *
   * @return The main content panel.
   */
  private JPanel createMainContentPanel(BasePanel panel) {
    JPanel searchPanel = createSearchPanel();
    JPanel imageGridPanel = createImageGridPanel();
    JScrollPane scrollPane = new JScrollPane(imageGridPanel);
    scrollPane.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    JPanel mainContentPanel = new JPanel();
    mainContentPanel.setLayout(
        new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
    mainContentPanel.add(searchPanel);
    mainContentPanel.add(scrollPane); // This will stretch to take up remaining space
    return mainContentPanel;
  }

  private JPanel createSearchPanel() {
    JPanel searchPanel = new JPanel(new BorderLayout());
    JTextField searchField = new JTextField(" Search");
    searchResult = new SearchResult();

    searchPanel.add(searchField, BorderLayout.CENTER);
    searchPanel.add(searchResult.getListComponent(), BorderLayout.SOUTH);

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
        });

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

              private void runSearch() {
                String query = searchField.getText();
                if (!query.trim().isEmpty()) {
                  Search.search(query); // Assumes Search class notifies SearchResult
                }
              }
            });

    return searchPanel;
  }

  /**
   * A JPanel is a container that holds and organizes other components.
   * It is used to group and layout other components within a graphical user
   * interface.
   */
  private JPanel createImageGridPanel() {
    JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2)); // 3 columns, auto rows
    File imageDir = new File(uploaded);

    if (imageDir.exists() && imageDir.isDirectory()) {
      File[] imageFiles = imageDir.listFiles((dir, name) -> name.matches(".*\\.(png|jpg|jpeg)"));
      if (imageFiles != null) {
        for (File imageFile : imageFiles) {
          ImageIcon imageIcon = new ImageIcon(
              new ImageIcon(imageFile.getPath())
                  .getImage()
                  .getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
          JLabel imageLabel = new JLabel(imageIcon);
          imageLabel.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                  App.imageViewer.displayImage(
                      " Explore 🐥 ",
                      imageFile.getPath()); // Call method to display the
                  // clicked image
                }
              });
          imageGridPanel.add(imageLabel);
        }
      }
    }
    return imageGridPanel;
  }
}
