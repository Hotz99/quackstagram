package explore;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import utils.AppPathsSingleton;

public abstract class Search {

  //singleton pattern
  private final static AppPathsSingleton appPathsSingleton = AppPathsSingleton.getInstance();
  private final static String users = appPathsSingleton.USERS; 
  private final static String imageDetails = appPathsSingleton.IMAGE_DETAILS; 
  private final static String[] searchPaths = appPathsSingleton.SEARCH_PATHS; 

  public static List<String> search(String query) {
    List<String> results = new ArrayList<>();
    if (query.isEmpty()) return results;

    // ** other way:

    //Path searchPath = determineSearchPath(query.charAt(0));
    //String searchQuery = query.substring(1);

    //if (searchPath!=null){
      //results.addAll(searchInFile(searchPath, searchQuery));
      // } else { for (String path:AppPAths.SEARCH_PATHS){results.addAll(searchInFile(Paths.get(path), query));}
      //} sout("found after searching" + results); return results;}
      // private static Path determineSearchPath(char firstChar){
        //switch(firstChar){case '@': return Paths.get(AppPaths.USERS);
        //case '#': return Paths.get(AppPaths.IMAGE_DETAILS); default: return null;}}
      //}

      //}
    

    if (query.charAt(0) == '@') {
      results.addAll(
        searchInFile(Paths.get(users), query.substring(1))
      );
    } else if (query.charAt(0) == '#') {
      results.addAll(
        searchInFile(Paths.get(imageDetails), query.substring(1))
      );
    } else {
      for (String path : searchPaths) {
        results.addAll(searchInFile(Paths.get(path), query));
      }
    }
    System.out.println("Found after searching: " + results);
    return results;
  }

  private static List<String> searchInFile(Path filePath, String query) {
    List<String> results = new ArrayList<>();

    String queryLower = query.toLowerCase();
    try (Stream<String> lines = Files.lines(filePath)) {
      lines.filter(line -> line.toLowerCase().contains(queryLower)).forEach(results::add);
    } catch (IOException ex) {
      System.out.println("Error reading from file: " + filePath);
      //ex.printStackTrace();
    }
    return results;
  }
}
