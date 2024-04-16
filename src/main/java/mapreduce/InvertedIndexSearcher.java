package mapreduce;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author NE Aarab
 */
public class InvertedIndexSearcher {

    public static List<Integer> search(Map<String, List<Integer>> index, String query) {
    	// Split the query into individual words and convert them to lower case
        String[] words = query.toLowerCase().split("\\W+");
        List<Integer> results = new ArrayList<>();

        for (String word : words) {
            if (index.containsKey(word)) {
            	// If this is the first word, add all its document IDs to the results
                if (results.isEmpty()) {
                    results.addAll(index.get(word));
                } else {
                	// otherwise, only keep those document IDs that are already in the results
                    // This ensures that the final list contains only document IDs where all words appear
                    results.retainAll(index.get(word));
                }
            } else {
            	// If any word in the query is not in the index, an empty list is returned
                return new ArrayList<>();
            }
        }
        return results;
    }
}
