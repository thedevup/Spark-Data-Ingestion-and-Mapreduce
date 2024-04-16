package sparkdataingestion;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

/**
 * 
 * @author NE Aarab
 */
public class DataProcessor {

    public Dataset<Row> performOperations(Dataset<Row> dataset) {
    	// Select relevant columns / The ones we use to perform operations here
        dataset = dataset.select("Player", "Pos", "Age", "Tm", "G", "PTS");
        
        // Removing duplicate rows
        dataset = dataset.dropDuplicates();

        // Drop rows with missing values
        dataset = dataset.na().drop();

        // Filtering players who played at least 5 games
        dataset = dataset.filter(dataset.col("G").gt(5));
        
        // Filtering players with age greater than 25 and position as follows 'SG'
        dataset = dataset.filter(dataset.col("Age").gt(25).and(dataset.col("Pos").equalTo("SG")));

        // Grouping and aggregating
        dataset = dataset.groupBy("Tm", "Pos").agg(functions.avg("PTS").alias("AvgPoints"));

        // Sorting by average points
        dataset = dataset.sort(functions.desc("AvgPoints"));

        // Return the dataset
        return dataset;
    }
}