package code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class StanfordSAToImdbMapper {

	private static final String URL_NEG_DIR_TRAIN = "T:\\E-BOOKS\\UMN books\\Machine Learning\\ML project\\SentimentAnalysis\\aclImdb\\train\\urls_neg.txt";
	private static final String URL_POS_DIR_TRAIN = "T:\\E-BOOKS\\UMN books\\Machine Learning\\ML project\\SentimentAnalysis\\aclImdb\\train\\urls_pos.txt";
	private static final String URL_NEG_DIR_TEST = "T:\\E-BOOKS\\UMN books\\Machine Learning\\ML project\\SentimentAnalysis\\aclImdb\\test\\urls_neg.txt";
	private static final String URL_POS_DIR_TEST = "T:\\E-BOOKS\\UMN books\\Machine Learning\\ML project\\SentimentAnalysis\\aclImdb\\test\\urls_pos.txt";
	
	private static final String DATA_DIR = "E:\\GitHub\\Projects\\MachineLearningProject\\src\\resources\\movies_and_scores.tsv";
	
	private static final String OUTPUT_DIR = "E:\\GitHub\\Projects\\MachineLearningProject\\src\\resources\\stanfordToImdbMap.tsv";
	
	public static void main(String[] args) throws IOException {
		
		List<String> urlNegTrainLines = FileUtils.readLines(new File(URL_NEG_DIR_TRAIN));
		List<String> urlPosTrainLines = FileUtils.readLines(new File(URL_POS_DIR_TRAIN));
		List<String> urlNegTestLines = FileUtils.readLines(new File(URL_NEG_DIR_TEST));
		List<String> urlPosTestLines = FileUtils.readLines(new File(URL_POS_DIR_TEST));
		
		List<String> dataLines = FileUtils.readLines(new File(DATA_DIR));
		
		HashMap<String, Integer> dataMap = buildDataMap(dataLines);
		HashMap<String, String> urlNegTrainMap = buildUrlMap(dataMap, urlNegTrainLines);
		HashMap<String, String> urlPosTrainMap = buildUrlMap(dataMap, urlPosTrainLines);
		HashMap<String, String> urlNegTestMap = buildUrlMap(dataMap, urlNegTestLines);
		HashMap<String, String> urlPosTestMap = buildUrlMap(dataMap, urlPosTestLines);
		
		printMap("neg_train", urlNegTrainMap, OUTPUT_DIR);
		printMap("pos_train", urlPosTrainMap, OUTPUT_DIR);
		printMap("neg_test", urlNegTestMap, OUTPUT_DIR);
		printMap("pos_test", urlPosTestMap, OUTPUT_DIR);
		
		System.out.println("Mapping and Printing complete.");
	}

	private static void printMap(String prefix, HashMap<String, String> urlMap,
			String outputDir) throws IOException {

		Set<String> mapKeys = urlMap.keySet();
		String outputString = "";
		
		for(String key: mapKeys)	{
			outputString += prefix+ "\t"+ key+ "\t" + urlMap.get(key)+ "\n";
//			System.out.println(prefix+ "\t"+ key+ "\t" + urlMap.get(key)+ "\n");
		}
		
//		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(OUTPUT_DIR), true));
//		writer.write(outputString);
//		writer.close();
		
	}

	private static HashMap<String, String> buildUrlMap(
			HashMap<String, Integer> dataMap, List<String> urlLines) {

		HashMap<String, String> urlMap = new HashMap<String, String>();
		
		int lineNr = 0;
		int count =0;
		for(String urlLine : urlLines)	{
			String[] splitLine = urlLine.split("/");
			String imdbId = splitLine[4];
			if(dataMap.containsKey(imdbId))	{
				if(urlMap.containsKey(imdbId))	{
					String oldValue = urlMap.get(imdbId);
					urlMap.put(imdbId, lineNr+ ";"+ oldValue);
//					count++;
				}
				else	{
					urlMap.put(imdbId, lineNr+ "");
//					count++;
				}
				
			}
			lineNr++;
		}
		
//		System.out.println("Count: "+ count);
		return urlMap;
	}

	private static HashMap<String, Integer> buildDataMap(List<String> dataLines) {

		HashMap<String, Integer> dataMap = new HashMap<String, Integer>();
		
		for(String dataLine : dataLines)	{
			String[] splitLine = dataLine.split("\t");
			String imdbId = splitLine[2];
			String tempId = "";
			if(imdbId.length() < 7)	{
				for(;imdbId.length()<7;)	{
					tempId = "0"+ imdbId;
					imdbId = tempId;
				}
			}
			tempId = "tt"+ imdbId;
			imdbId = tempId;
			if(!dataMap.containsKey(imdbId))	{
				dataMap.put(imdbId, 1);
			}
		}

		return dataMap;
	}

}
