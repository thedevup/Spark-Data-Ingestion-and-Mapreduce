package mapreduce;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import conf.Settings;


/**
 * 
 * @author NE Aarab
 */
public class InvertedIndexMain {

    public static void main(String[] args) {
    	ConcurrentHashMap<String, List<Integer>> index;

    	// check for an existing index file and load it. If it doesn't exist, build a new one and save it
        File indexFile = new File(Settings.SAVE_INDEX);
        if (indexFile.exists()) {
            index = loadIndex();
        } else {
            index = InvertedIndexBuilder.buildIndex();
            InvertedIndexBuilder.saveIndex(index);
        }
    	
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter search query, words should be separated by space:");
        while (true) {
            String query = scanner.nextLine().trim();
            // In here, we first check if the input is empty or not, if empty,then we ask the user again
            if (!query.isEmpty()) {
                List<Integer> results = InvertedIndexSearcher.search(index, query);
                System.out.println("Documents containing the query: " + results);
                break;
            } else {
                System.out.println("The input is empty, please enter a valid query:");
            }
        }
        scanner.close();
    }
    
    // Load the index, check location in the Settings class
    // returns null if there's an error
    private static ConcurrentHashMap<String, List<Integer>> loadIndex() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(Settings.SAVE_INDEX))) {
            return (ConcurrentHashMap<String, List<Integer>>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading index from file: " + e.getMessage());
            return null;
        }
    }
}
