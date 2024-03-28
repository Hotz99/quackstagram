package explore;

import java.util.List;

public interface SearchObserver {
  void onSearchResults(List<String> results);
}
