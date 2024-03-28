package explore;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import utils.AppPathsSingleton;

public abstract class Search {

  private static final AppPathsSingleton appPathsSingleton = AppPathsSingleton.getInstance();
  private static final String users = appPathsSingleton.USERS;
  private static final List<SearchObserver> observers = new ArrayList<>();

  public static void addSearchObserver(SearchObserver observer) {
    observers.add(observer);
  }

  public static void removeSearchObserver(SearchObserver observer) {
    observers.remove(observer);
  }

  private static void notifyObservers(List<String> results) {
    for (SearchObserver observer : observers) {
      observer.onSearchResults(results);
    }
    System.out.println("Notified " + observers.size() + " observers.");
  }

  public static List<String> search(String query) {
    List<String> results = new ArrayList<>();
    if (query.isEmpty()) {
      notifyObservers(results);
      return results;
    }

    results.addAll(searchInFile(Paths.get(users), query));
    notifyObservers(results);
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
