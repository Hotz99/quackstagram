package explore;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import app.App;
import post.PostImageViewer;
import utils.BasePanel;
import utils.HeaderFactory;

/**
 * The ExplorePanel class represents a panel that displays the explore
 * functionality of the application.
 * It extends the BasePanel class and provides methods to create the main
 * content panel, search panel, image grid panel,
 * and other UI components related to exploring and displaying images.
 */
public class ExplorePanel extends BasePanel {

  private final int IMAGE_SIZE = App.getAppWidth() / 3; // Size for each image in the grid

  private SearchResult searchResult; // Instance variable for SearchResult

  private static ExplorePanel instance;

  public static ExplorePanel getInstance() {
    if (instance == null) {
      instance = new ExplorePanel();
    }
    return instance;
  }

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
    JPanel mainContentPanel = createMainContentPanel();

    // Add panels to the frame
    add(headerPanel, BorderLayout.NORTH);
    add(mainContentPanel, BorderLayout.CENTER);
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
  private JPanel createMainContentPanel() {
    JScrollPane scrollPane = new JScrollPane(createImageGridPanel(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    JPanel mainContentPanel = new JPanel();
    mainContentPanel.setLayout(
        new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
    mainContentPanel.add(createSearchPanel());
    mainContentPanel.add(scrollPane); // This will stretch to take up remaining space
    return mainContentPanel;
  }

  private JPanel createSearchPanel() {
    JPanel searchPanel = new JPanel(new BorderLayout());
    JTextField searchField = new JTextField(" Search ...");
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
    File imageDir = new File(appPaths.UPLOADED);

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
                  System.out.println(imageFile.getPath());

                  App.getPostViewer().displayImage(
                      imageFile.getPath(),
                      PostImageViewer.ImageType.EXPLORE);
                }
              });
          imageGridPanel.add(imageLabel);
        }
      }
    }
    return imageGridPanel;
  }
}
