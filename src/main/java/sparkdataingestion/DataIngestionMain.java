package sparkdataingestion;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import conf.Settings;

/**
 * 
 * @author NE Aarab
 */
public class DataIngestionMain {

    public static void main(String[] args) {
    	// The master is set to 'local' since we want to use our machine for processing
        SparkSession spark = SparkSession
                .builder()
                .master("local")
                .getOrCreate();

        // See the Settings class to specify path to the csv file
        Dataset<Row> dataset = spark.read().option("header", "true").csv(Settings.SPARK_INGESTION_DATASET);
        
        DataProcessor processor = new DataProcessor();
        // Call methods from DataProcessor
        Dataset<Row> processedData = processor.performOperations(dataset);

        // Show the  processed data
        processedData.show();

        spark.stop();
    }
}

