package explore;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import app.App;
import user.User;

public abstract class SearchResult {

    private User user;
    private String contentPath;
    
    private static Set<String> queriedUsernames = new HashSet<>();

    public static void setQueryUsernames(Set<String> newQueriedUsernames) {
        queriedUsernames = newQueriedUsernames;
    }
    
    public JList createSearchResultList(Set<String> results) {
        JList<String> list = new JList(results.toArray());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                    String selectedUsername = (String) list.getSelectedValue();
                    App.showPanelWithUsername(selectedUsername);
                }
            }
        });

        return list;
    }
    
}

