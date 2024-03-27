package explore;

import app.App;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public abstract class SearchResult {

  public static JList createSearchResultList(List<String> results) {
    JList<String> list = new JList(results.toArray());
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setLayoutOrientation(JList.VERTICAL);
    list.setVisibleRowCount(-1);

    JPanel cards = App.getCards();
    AtomicReference<ExplorePanel> explorePanelRef = new AtomicReference<>();

    for (Component card : cards.getComponents()) {
      if (card instanceof ExplorePanel) {
        // clear card
        ((ExplorePanel) card).closeOverlayComponents();
        explorePanelRef.set((ExplorePanel) card);
        break;
      }
    }

    list.addListSelectionListener(
        new ListSelectionListener() {
          @Override
          public void valueChanged(ListSelectionEvent evt) {
            if (!evt.getValueIsAdjusting()) {
              String selectedUsername = (String) list.getSelectedValue();

              System.out.println(selectedUsername);
              ExplorePanel explorePanel = explorePanelRef.get();
              if (explorePanel != null) {
                explorePanel.closeOverlayComponents();
                App.showPanelWithUsername(selectedUsername);
              }
            }
          }
        });

    ExplorePanel explorePanel = explorePanelRef.get();
    if (explorePanel != null) {
      explorePanel.overlayComponent(new JScrollPane(list));
    } else {
      System.out.println("No ExplorePanel found in cards.");
    }
    return list;
  }
}
