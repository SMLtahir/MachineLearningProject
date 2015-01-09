package code;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class CopyPasteFiles {

	private static final String COPY_DIR = "T:/E-BOOKS/UMN books/Machine Learning/ML project/SentimentAnalysis/aclImdb/";
	private static final String PASTE_DIR = "E:/Eclipse/de.tudarmstadt.ukp.dkpro.tc.experiment.pythagoras/src/main/java/mlProjectNlp/Resources/";
	private static final String COPY_TEST_NEG = COPY_DIR+ "test/neg"; 
	private static final String COPY_TRAIN_NEG = COPY_DIR+ "train/neg";
	private static final String COPY_TEST_POS = COPY_DIR+ "test/pos";
	private static final String COPY_TRAIN_POS = COPY_DIR+ "train/pos";
	
	private static final String StanToImdb_Path= "E:/GitHub/Projects/MachineLearningProject/src/resources/stanfordToImdbMap.tsv";
	
	public static void main(String[] args) throws IOException {

		List<String> stanToImdbLines = FileUtils.readLines(new File(StanToImdb_Path));
		
		List<File> trainNegPaths = Arrays.asList(new File(COPY_TRAIN_NEG).listFiles());
		List<File> trainPosPaths = Arrays.asList(new File(COPY_TRAIN_POS).listFiles());
		List<File> testNegPaths = Arrays.asList(new File(COPY_TEST_NEG).listFiles());
		List<File> testPosPaths = Arrays.asList(new File(COPY_TEST_POS).listFiles());

		
		for(String line: stanToImdbLines)	{
			String[] splitLine = line.split("\t");
			String dirName = splitLine[0];
			String imdbId = splitLine[1];
			String[] fileIds = splitLine[2].split(";");
			
			if(dirName.equals("neg_train"))	{
				for(String id: fileIds)	{					
					for(File trainNegFile: trainNegPaths)	{
						String fileName = trainNegFile.getName();
						if(fileName.startsWith(id+"_"))	{
							String rating = fileName.split("_")[1].substring(0, 1);
							FileUtils.copyFile(trainNegFile, new File(PASTE_DIR+ imdbId+ "_"+ rating+ ".txt"));
							System.out.println();
						}
								
					
					}
					
				}
			}
			else if(dirName.equals("pos_train"))	{
				for(String id: fileIds)	{					
					for(File trainPosFile: trainPosPaths)	{
						String fileName = trainPosFile.getName();
						if(fileName.startsWith(id+"_"))	{
							String rating = fileName.split("_")[1].substring(0, 1);
							FileUtils.copyFile(trainPosFile, new File(PASTE_DIR+ imdbId+ "_"+ rating+ ".txt"));
							System.out.println();
						}
								
					
					}
					
				}
			}
			else if(dirName.equals("neg_test"))	{
				for(String id: fileIds)	{					
					for(File testNegFile: testNegPaths)	{
						String fileName = testNegFile.getName();
						if(fileName.startsWith(id+"_"))	{
							String rating = fileName.split("_")[1].substring(0, 1);
							FileUtils.copyFile(testNegFile, new File(PASTE_DIR+ imdbId+ "_"+ rating+ ".txt"));
							System.out.println();
						}
					}
					
				}
			}
			else if(dirName.equals("pos_test"))	{
				for(String id: fileIds)	{					
					for(File testPosFile: testPosPaths)	{
						String fileName = testPosFile.getName();
						if(fileName.startsWith(id+"_"))	{
							String rating = fileName.split("_")[1].substring(0, 1);
							FileUtils.copyFile(testPosFile, new File(PASTE_DIR+ imdbId+ "_"+ rating+ ".txt"));
							System.out.println();
						}
					}
					
				}
			}
		
			System.out.println();
		}
		System.out.println();
	}

}
