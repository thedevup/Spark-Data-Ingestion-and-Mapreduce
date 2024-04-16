package mapreduce;

import conf.Settings;
import framework.Mapper;
import framework.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author NE Aarab
 */
public class InvertedIndexBuilder {

	// The number of threads in the thread pool
    private static final int THREAD_POOL_SIZE = 4;

    public static ConcurrentHashMap<String, List<Integer>> buildIndex() {
    	// Locate the folder where the text files are placed - check the Settings class in conf
        File folder = new File(Settings.TEXT_FILES);
        File[] listOfFiles = folder.listFiles();
        ConcurrentHashMap<String, List<Integer>> index = new ConcurrentHashMap<>();

        if (listOfFiles != null) {
        	// Here we create a thread pool to process files concurrently
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            for (File file : listOfFiles) {
                if (file.isFile()) {
                	// Here we process each file in a separate thread
                    executor.execute(() -> processFile(file, index));
                }
            }
            
            // The executor is shutdown waiting for all the tasks to finish
            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
            	// Handle terminations
                Thread.currentThread().interrupt();
            }
        }
        return index;
    }

    private static void processFile(File file, ConcurrentHashMap<String, List<Integer>> index) {
    	// The text files from https://www.gutenberg.org/ have several different encodings, so we have to check them accordingly
        List<String> lines = new ArrayList<>();
        try {
            // First we try reading with UTF-8 encoding
            lines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            try {
                // If UTF-8 fails, we try ISO-8859-1
                lines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.ISO_8859_1);
            } catch (IOException ex) {
                // If ISO-8859-1 also fails, we log and skip this file
                System.err.println("Skipping file due to encoding issues: " + file.getName());
            }
        }

        // Define the mapper logic to split lines into words and map them to document IDs
        Mapper<String, String, String, Integer> mapper = (key, value) -> {
            List<Pair<String, Integer>> pairs = new ArrayList<>();
            // Split the 'value' into an array  of words
            String[] words = value.split("\\W+");
            for (String word : words) {
                pairs.add(new Pair<>(word.toLowerCase(), file.getName().hashCode()));
            }
            return pairs;
        };

        // Process each line in the file using the mapper
        // Keep in mind, the reducer logic is implemented here in line 95
        for (String line : lines) {
            List<Pair<String, Integer>> mappedPairs = mapper.map(file.getName(), line);
            for (Pair<String, Integer> pair : mappedPairs) {
                // Use merge method of ConcurrentHashMap to apply the reducer logic
            	// ConcurrentHashMap is already providing thread-safe behavior, and we are working within the same JVM
            	// so no need for BlockingQueue
            	// See: https://docs.oracle.com/en/java/javase/18/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html
                index.merge(pair.getKey(), new ArrayList<>(List.of(pair.getValue())), (existingVal, newVal) -> {
                    HashSet<Integer> mergedSet = new HashSet<>(existingVal);
                    mergedSet.addAll(newVal);
                    return new ArrayList<>(mergedSet);
                });
            }
        }
    }
    
    // Save the index, check the class Settings for the path
    // This function is called within the 'InvertedIndexMain' Class
    public static void saveIndex(ConcurrentHashMap<String, List<Integer>> index) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(Settings.SAVE_INDEX))) {
        	// See: https://www.programiz.com/java-programming/objectoutputstream
            out.writeObject(index); // Serialize and write the index object to the file.
        } catch (IOException e) {
            System.err.println("Error saving index to file: " + e.getMessage());
        }
    }
}
