package explore;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import utils.AppPathsSingleton;

public abstract class Search {

  private static final AppPathsSingleton appPathsSingleton = AppPathsSingleton.getInstance();
  private static final String users = appPathsSingleton.USERS;
  private static final List<SearchListener> listeners = new ArrayList<>();

  public static void addSearchListener(SearchListener listener) {
    listeners.add(listener);
  }

  public static void removeSearchListener(SearchListener listener) {
    listeners.remove(listener);
  }

  private static void notifyListeners(List<String> results) {
    for (SearchListener listener : listeners) {
      listener.onSearchResults(results);
    }
    System.out.println("Notified " + listeners.size() + " listeners.");
  }

  public static List<String> search(String query) {
    List<String> results = new ArrayList<>();
    if (query.isEmpty()) {
      notifyListeners(results);
      return results;
    }

    results.addAll(searchInFile(Paths.get(users), query));
    notifyListeners(results);
    return results;
  }

  private static Set<String> searchInFile(Path filePath, String query) {
    Set<String> results = new HashSet<>();
    String queryLower = query.toLowerCase();
    try (Stream<String> lines = Files.lines(filePath)) {
      lines
        .filter(line -> line.toLowerCase().contains(queryLower))
        .map(line -> line.split(":")[0])
        .forEach(results::add);
    } catch (IOException ex) {
      System.out.println("Error reading from file: " + filePath);
    }
    return results;
  }
}
