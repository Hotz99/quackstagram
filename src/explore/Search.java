package explore;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import utils.AppPathsSingleton;

public abstract class Search {

  // singleton pattern
  private static final AppPathsSingleton appPathsSingleton = AppPathsSingleton.getInstance();
  private static final String users = appPathsSingleton.USERS;

  private static Set<String> usernames = new HashSet<>();

  public static List<String> search(String query) {
    List<String> results = new ArrayList<>();
    if (query.isEmpty())
      return results;

    // Clear the usernames set before a new search
    usernames.clear();

    // if (query.charAt(0) == '@') {
    results.addAll(searchInFile(Paths.get(users), query));
    // } else {
    // System.out.println("Invalid query. User search should start with '@'.");
    // }

    return results;
  }

  private static Set<String> searchInFile(Path filePath, String query) {
    Set<String> results = new HashSet<>();

    String queryLower = query.toLowerCase();
    try (Stream<String> lines = Files.lines(filePath)) {
      lines
          .filter(line -> line.toLowerCase().contains(queryLower))
          .map(line -> line.split(":")[0]) // Extract the username
          .forEach(result -> {
            results.add(result);
            usernames.add(result); // Add the username to the usernames set
          });
    } catch (IOException ex) {
      System.out.println("Error reading from file: " + filePath);
      // ex.printStackTrace();
    }
    return results;
  }

  // Getter for the Set of usernames in the current query
  public static Set<String> getUsernames() {
    return usernames;
  }
}
