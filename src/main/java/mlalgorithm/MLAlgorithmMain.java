package mlalgorithm;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * 
 * @author NE Aarab
 */
public class MLAlgorithmMain {

    public static void main(String[] args) {
    	// Same as in assignment 2 (spark data ingestion)
        SparkSession spark = SparkSession
            .builder()
            .master("local")
            .getOrCreate();

        DataPreparer dataPreparer = new DataPreparer();

        // Data preparation
        Dataset<Row> preparedData = dataPreparer.prepareData(spark);
        
        preparedData.show();
        
        // In here, code for training and evaluation could be added
        // More pre-processing is still needed
        spark.stop();
    }
}