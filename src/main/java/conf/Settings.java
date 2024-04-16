package conf;

/**
 * 
 * @author NE Aarab
 */
public class Settings {
	// Insert the path to the text files here
	// The files downloaded from https://www.gutenberg.org/ are all unzipped and put in one directory
	public static final String TEXT_FILES = "/Users/gebruiker/Desktop/data_assingmnet_DECC/www.gutenberg.org/reduced_extracted_files";
	// Path to the file where indexes will be saved
	public static final String SAVE_INDEX =  "/Users/gebruiker/Desktop/data_assingmnet_DECC/www.gutenberg.org/index_file.ser";
	// Path to the dataset from Kaggle for ingestion
	public static final String SPARK_INGESTION_DATASET = "/Users/gebruiker/Desktop/data_assingmnet_DECC/www.gutenberg.org/NBA_player_stats.csv";
	// Path to the dataset from Kaggle for Dirichlet Allocation
	public static final String DIRICHLET_ALLOCATION_DATASET = "/Users/gebruiker/Desktop/data_assingmnet_DECC/www.gutenberg.org/Mental-Health-Twitter.csv";
}
