package mlalgorithm;

import org.apache.spark.sql.SparkSession;
import conf.Settings;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.ml.feature.StopWordsRemover;

/**
 * 
 * @author NE Aarab
 */
public class DataPreparer {

    public Dataset<Row> prepareData(SparkSession spark) {
    	
        // Loading the dataset
        Dataset<Row> dataset = spark.read().option("header", "true").csv(Settings.DIRICHLET_ALLOCATION_DATASET);

        // Remove stop words
        // See the NLP course
        // See: https://spark.apache.org/docs/latest/api/python/reference/api/pyspark.ml.feature.StopWordsRemover.html#:~:text=,uid%20and%20some%20extra%20params
        StopWordsRemover remover = new StopWordsRemover()
                .setInputCol("words")
                .setOutputCol("filtered");
        Dataset<Row> filteredData = remover.transform(dataset);

        return filteredData;
    }
}