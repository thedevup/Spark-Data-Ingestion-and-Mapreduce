package mapreduce;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import static org.junit.jupiter.api.Assertions.*;


/**
 * 
 * @author NE Aarab
 */
class InvertedIndexSearcherTest {

    @Test
    void testSearch() {
        // We create a simple index
        ConcurrentHashMap<String, List<Integer>> index = new ConcurrentHashMap<>();
        index.put("test", List.of(1, 2));
        index.put("example", List.of(2));

        // Test searching for a single word
        List<Integer> testOne = InvertedIndexSearcher.search(index, "test");
        assertEquals(List.of(1, 2), testOne, "This is a test example");

        // Test searching for a word not in the index
        List<Integer> testTwo = InvertedIndexSearcher.search(index, "something");
        assertTrue(testTwo.isEmpty(), "This is a test example");

        // Test searching for multiple words
        List<Integer> testThree = InvertedIndexSearcher.search(index, "test example");
        assertEquals(List.of(2), testThree, "This is a test example");
    }
}
