package explore;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.text.html.ListView;

import user.User;

public abstract class SearchResult {

    private User user;
    private String contentPath;
    
    private static Set<String> queriedUsernames = new HashSet<>();

    public static void setQueryUsernames(Set<String> newQueriedUsernames) {
        queriedUsernames = newQueriedUsernames;
    }
    
    

    public ListView createSearchResultList(Set<String> results) {
        JList<String> list = new JList(results);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);


    }

   
    
}
    

