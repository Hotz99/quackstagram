package explore;

import java.util.*;

import database.repositories.UserRepository;

public abstract class Search {
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

    results.addAll(UserRepository.getInstance().fuzzyFindUsernames(query));
    notifyObservers(results);
    return results;
  }
}
