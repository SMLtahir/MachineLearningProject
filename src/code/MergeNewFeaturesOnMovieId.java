package code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;


//After running this, it is still required to go to the newly created merged file and manually interchange the class label row with the last row.
public class MergeNewFeaturesOnMovieId {

	private static final String NEW_FEATURE_INPUT_PATH = "src/resources/movie_directors_in_top_100.tsv";
	private static final String OLD_FEATURES_INPUT_PATH = "src/resources/movies_and_scores.tsv";
	private static final String OUTPUT_FILE_PATH = "src/resources/mergedMovies_and_scores.tsv";
	private static final String SEPARATOR = "\t";
	
	public static void main(String[] args) throws IOException {

		HashMap<Integer, String> newFeatureMap = new HashMap<Integer, String>(); 
		List<String> newFeaturesLines = FileUtils.readLines(new File(NEW_FEATURE_INPUT_PATH));
		String outputString = "";
		
		boolean isHeader = false;
		String newHeader = "";
		//Set the new features for every movie ID
		System.out.println("Setting the new features for every movie ID");
		for(String line: newFeaturesLines)	{
			if(isHeader == false && line.contains("MovieId"))	{
				String[] headerParams = line.split(SEPARATOR);
				newHeader = "";
				for(int i=1; i< headerParams.length; i++)	{
					newHeader += headerParams[i] + SEPARATOR;
				}
				isHeader = true;
				continue;
			}
			else if(isHeader == true)	{
				String[] newFeatureParams = line.split(SEPARATOR);
				int movieId = Integer.parseInt(newFeatureParams[0]);
				
				String newFeatures = "";
				for(int i=1; i< newFeatureParams.length; i++)	{
					newFeatures += newFeatureParams[i] + SEPARATOR;
				}
				
				newFeatureMap.put(movieId, newFeatures);	
			}
			
		}
		
		//Merge old features into new features
		System.out.println("Merging old features into new features");
		List<String> oldFeaturesLines = FileUtils.readLines(new File(OLD_FEATURES_INPUT_PATH));
		isHeader = false;
		String oldHeader = "";
		for(String line: oldFeaturesLines)	{
			String[] oldFeatureParams = line.split(SEPARATOR);
			
			if(isHeader == false && line.contains("id"))	{
				String[] headerParams = line.split(SEPARATOR);				
				oldHeader = "";
				for(int i=0; i< headerParams.length; i++)	{
					oldHeader += headerParams[i] + SEPARATOR;
				}
				isHeader = true;
				outputString += oldHeader + newHeader + "\n"; 
				continue;
			}
			else if(isHeader == true)	{
				int mId = Integer.parseInt(oldFeatureParams[0]);	
				if(newFeatureMap.containsKey(mId) )	{
					outputString += line + SEPARATOR+ newFeatureMap.get(mId) + "\n";	
				}
			}
				
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(OUTPUT_FILE_PATH)));
		writer.write(outputString);
		writer.close();
	}

}
