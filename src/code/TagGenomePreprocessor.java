package code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

public class TagGenomePreprocessor {

	private static final String TAG_RELEVANCE = "T:/E-BOOKS/UMN books/Machine Learning/ML project/tag-genome/tag-genome/tag_relevance.dat";
	private static final String TAGS = "T:/E-BOOKS/UMN books/Machine Learning/ML project/tag-genome/tag-genome/tags.dat";
	private static final String WRITE_PATH = "T:/E-BOOKS/UMN books/Machine Learning/ML project/tag-genome/tag-genome/movieTagGenome.tsv";
	
	public static void main(String[] args) throws IOException {

		//List most popular tags only
		List<String> tagLines = FileUtils.readLines(new File(TAGS));
		TreeMap<Integer, String> popularTagsMap = setMapOfPopularTags(tagLines);
		
		//Map movie to list of popular tags
		List<String> movieLines = FileUtils.readLines(new File(TAG_RELEVANCE));
		TreeMap<Integer, String> movieTagsMap = setMovieTagMap(popularTagsMap, movieLines);
		
		writeMovieTagsMapToFile(movieTagsMap, popularTagsMap, WRITE_PATH);
	}

	private static void writeMovieTagsMapToFile(
			TreeMap<Integer, String> movieTagsMap,
			TreeMap<Integer, String> popularTagsMap, String writePath) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(WRITE_PATH), true));
		
		//Write header
		String outputString = "MovieId"+ "\t";
		for(int key: popularTagsMap.keySet())	{
			outputString += popularTagsMap.get(key)+ "\t";
		}
		outputString += "\n";
		writer.write(outputString);
		
		float progress = 0;
		int count = 0;
		int totalSize = movieTagsMap.size();
		Set<Integer> movieTagsKeySet = movieTagsMap.keySet();
		
		//Write main mapping
		for(int key: movieTagsKeySet)	{
			count++;
			outputString = key+ "\t"+ movieTagsMap.get(key)+ "\n";
			writer.write(outputString);
			progress = (float) count/ (float) totalSize;
			System.out.printf("Progress: %.2f %% \n", (100*progress));
		}
		
		writer.close();
	}

	private static TreeMap<Integer, String> setMovieTagMap(
			TreeMap<Integer, String> popularTagsMap, List<String> movieLines) {

		TreeMap<Integer, String> movieTagsMap = new TreeMap<Integer, String>();
		boolean isHeaderCrossed = false;
		float progress = 0;
		int count = 0;
		
		for(String line: movieLines)	{
			count++;
			if(!isHeaderCrossed)	{
				isHeaderCrossed = true;
				continue;
			}
			
			String[] splitLine = line.split("\t");
			int movieId = Integer.parseInt(splitLine[0]);
			int tagId = Integer.parseInt(splitLine[1]);
			double relevance = Double.parseDouble(splitLine[2]);
			String oldTagValues = "";
			
			if(popularTagsMap.containsKey(tagId))	{
				if(movieTagsMap.containsKey(movieId))	{
					oldTagValues = movieTagsMap.get(movieId);
//					System.out.print(movieTagsMap.get(movieId));
				}
				oldTagValues += relevance+ "\t";
				movieTagsMap.put(movieId, oldTagValues);
			}
			progress = (float) count/ (float) movieLines.size();
			System.out.printf("Progress: %.2f %% \n", (100*progress));
		}
		
		return movieTagsMap;
	}

	private static TreeMap<Integer, String> setMapOfPopularTags(
			List<String> tagLines) {

		TreeMap<Integer, String> mapOfPopularTags = new TreeMap<Integer, String>();
		boolean isHeaderCrossed = false;
		
		for(String line: tagLines)	{
			if(!isHeaderCrossed)	{
				isHeaderCrossed = true;
				continue;
			}
			
			String[] splitLine = line.split("\t");
			int tagId = Integer.parseInt(splitLine[0]);
			String tag = splitLine[1];
			int tagPopularity = Integer.parseInt(splitLine[2]);
			
			if(tagPopularity>= 50)	{
				mapOfPopularTags.put(tagId, tag);
			}
		}
		
		return mapOfPopularTags;
	}

}
