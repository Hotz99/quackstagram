package explore;

import app.App;
import java.util.List;
import javax.swing.*;

public class SearchResult implements SearchObserver {

  private JList<String> list;
  private JScrollPane scrollPane;

  public SearchResult() {
    list = new JList<>();
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setLayoutOrientation(JList.VERTICAL);
    list.setVisibleRowCount(-1);

    Search.addSearchObserver(this);

    list.addListSelectionListener(evt -> {
      if (!evt.getValueIsAdjusting()) {
        String selectedUsername = list.getSelectedValue();
        System.out.println("Selected username: " + selectedUsername);
        App.showProfileByUsername(selectedUsername);

        ExplorePanel explorePanel = ExplorePanel.getInstance();
        if (explorePanel != null) {
          explorePanel.closeOverlayComponents();
        } else {
          System.out.println("No ExplorePanel found.");
        }
      }
    });

    scrollPane = new JScrollPane(list);
  }

  @Override
  public void onSearchResults(List<String> results) {
    System.out.println("Received " + results.size() + " search results.");
    SwingUtilities.invokeLater(() -> {
      DefaultListModel<String> model = new DefaultListModel<>();
      for (String result : results) {
        model.addElement(result);
      }
      list.setModel(model);

      updateExplorePanel();
    });
  }

  public JComponent getListComponent() {
    return scrollPane;
  }

  private void updateExplorePanel() {
    ExplorePanel explorePanel = ExplorePanel.getInstance(); // Assuming getInstance() gets the current instance
    if (explorePanel != null) {
      explorePanel.closeOverlayComponents();
      explorePanel.overlayComponent(scrollPane); // Use existing JScrollPane
    } else {
      System.out.println("No ExplorePanel found.");
    }
  }
}
