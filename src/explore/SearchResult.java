package explore;

import app.App;
import java.util.List;
import javax.swing.*;

public class SearchResult implements SearchObserver {
  // this causes instantion recursion
  // private ExplorePanel explorePanel = ExplorePanel.getInstance();

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

        ExplorePanel.getInstance().closeOverlayComponents();
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
    ExplorePanel.getInstance().closeOverlayComponents();
    ExplorePanel.getInstance().overlayComponent(scrollPane);
  }
}
