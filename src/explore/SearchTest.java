package explore;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SearchTest {
    
    void testSearchWithUsername() {
        // Test searching by username (e.g., "@user")
        assertTrue(Search.search("@user").contains("Expected result"));
    }
    
 
    void testSearchWithHashtag() {
        // Test searching by hashtag (e.g., "#hashtag")
        assertTrue(Search.search("#hashtag").contains("Expected result"));
    }
    
   
    void testEmptyQuery() {
        // Test with an empty query
        assertEquals(0, Search.search("").size());
    }
}
