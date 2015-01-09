package code;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class prepareBinaryDataset {

	private static final String PASTE_DIR = "E:\\Eclipse\\de.tudarmstadt.ukp.dkpro.tc.experiment.pythagoras\\src\\main\\resources\\mlProjectResources\\FullData\\";
	private static final String COPY_DIR = "E:\\Eclipse\\de.tudarmstadt.ukp.dkpro.tc.experiment.pythagoras\\src\\main\\resources\\mlProjectResources\\Full\\ClassB\\";
	
	private static final String Mapped_Data_Path= "E:\\GitHub\\Projects\\MachineLearningProject\\src\\resources\\movies_and_scores_new.tsv";
	
	public static void main(String[] args) throws IOException {

		List<File> fileList = Arrays.asList(new File(COPY_DIR).listFiles());
		List<String> dataLines = FileUtils.readLines(new File(Mapped_Data_Path));
		
		HashMap<String, String> idLabelMap = buildIdLabelMap(dataLines);
		buildBinaryDataset(idLabelMap, fileList);
	}

	private static void buildBinaryDataset(HashMap<String, String> idLabelMap,
			List<File> fileList) throws IOException {

		for(File file: fileList)	{
			String fileName = file.getName();
			String imdbId = fileName.split("_")[0];
			if(idLabelMap.containsKey(imdbId))	{
				String label = idLabelMap.get(imdbId);
				FileUtils.copyFile(file, new File(PASTE_DIR+ label+ "_"+ fileName));	
			}
//			System.out.println();
		}
		
	}

	private static HashMap<String, String> buildIdLabelMap(
			List<String> dataLines) {

		HashMap<String, String> idLabelMap = new HashMap<String, String>();
		
		for(String dataLine : dataLines)	{
			String[] splitLine = dataLine.split("\t");
			String imdbId = splitLine[2];
			String label = splitLine[66];
			String tempId = "";
			if(imdbId.length() < 7)	{
				for(;imdbId.length()<7;)	{
					tempId = "0"+ imdbId;
					imdbId = tempId;
				}
			}
			tempId = "tt"+ imdbId;
			imdbId = tempId;
			if(!idLabelMap.containsKey(imdbId))	{
				idLabelMap.put(imdbId, label);
			}
		}

		return idLabelMap;
	}

}
