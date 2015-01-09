package code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class wekaGenerator {

//	private static final String INPUTPATH = "src/resources/testData.tsv";
	private static final String INPUTPATH = "src/resources/final_data.csv";
	
	private static final String OUTPUTPATH = "src/resources/finalData_Train1990To1999WekaOutput.arff";
//	private static final String OUTPUTPATH = "src/resources/finalData_testWekaOutput.arff";
	
	private static final String SEPARATOR = ",";
	private static String[] HEADER;
	private static final int NR_OF_ATTRIBUTES = 634;
	private static int[] excludeArray = {0,1,2,17,50}; 
	
	public static void main(String[] args) throws IOException {

		List<String> dataStrings = FileUtils.readLines(new File(INPUTPATH));
		List<Integer> excludeList = initializeExcludeList(excludeArray);
		RealMatrix dataMatrix = getData(dataStrings, excludeList);
		createWekaFile(OUTPUTPATH,dataMatrix, excludeList);
		
		System.out.println("Test complete");
	}
	
	
	private static List<Integer> initializeExcludeList(int[] excludedArray) {

		List<Integer> excludeList = new ArrayList<Integer>();
		for(int i=0; i< excludedArray.length; i++)	{
			excludeList.add(excludedArray[i]);
		}
		return excludeList;
	}


	private static void createWekaFile(String outputdirectory, RealMatrix dataMatrix, List<Integer> excludeList) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(OUTPUTPATH)));
		String outputString = "";
		outputString = "@relation G49Project" + "\n\n";
		writer.write(outputString);
		
		//For total number of attributes
		for(int i=0; i< NR_OF_ATTRIBUTES-1; i++)	{
			if(excludeList.contains(i) && i!=50)	{
				continue;
			}
			else if(i!= 50)	{
				if(!HEADER[i].contains(" "))	{
					outputString = "@attribute "+ HEADER[i]+ " numeric\n";	
					writer.write(outputString);
					continue;
				}
				else	{
					String header = HEADER[i].replace(" ", "_");
					outputString = "@attribute "+ header+ " numeric\n";	
					writer.write(outputString);
					continue;	
				}
				
			}
			else	{
				outputString = "@attribute "+ "boxOfficeClass"+ " {0,1}\n";
				writer.write(outputString);
			}
				
		}
		
		outputString = "\n@data\n\n";
		writer.write(outputString);
		
		//Print from Data Matrix
		for(int i=0; i< dataMatrix.getRowDimension(); i++)	{
			if(dataMatrix.getEntry(i, 2) < 1990 || dataMatrix.getEntry(i, 2) > 1999)	{
				System.out.print("");
				continue;
			}
			System.out.printf("Progress: %.2f%% \n", (100*(float)i/(float)(dataMatrix.getRowDimension()-1) ));
			for(int j=0; j< dataMatrix.getColumnDimension(); j++)	{
				if(!excludeList.contains(j) ||j==50)	{
					if(j<dataMatrix.getColumnDimension()-1)	{
						outputString = String.valueOf(dataMatrix.getEntry(i, j)) + ",";
						writer.write(outputString);
						continue;
					}
					else if(j == dataMatrix.getColumnDimension()-1)	{
						outputString = String.valueOf(dataMatrix.getEntry(i, j));
						writer.write(outputString);
						continue;
					}
					
				}
			}
			outputString = "\n";
			writer.write(outputString);
		}
		
		writer.close();
	}
	
	private static RealMatrix getData(List<String> dataStrings, List<Integer> excludeList) {

		int nrOfInstances = dataStrings.size()-1;
		RealMatrix dataMatrix = new Array2DRowRealMatrix(nrOfInstances, NR_OF_ATTRIBUTES);
		boolean isHeaderStored = false;
		for(int i=0; i< dataStrings.size(); i++)	{
			String dataString = dataStrings.get(i);
			if(isHeaderStored == false && dataString.contains("id"))	{
				HEADER = dataString.split(SEPARATOR);
				System.out.println(dataString);
				isHeaderStored = true;
			}
			
			else	{
				String[] dataLine = dataString.split(SEPARATOR);
				
				//Set data matrix based on attributes needed to be picked
				int j=0;
				for(j=0; j< NR_OF_ATTRIBUTES; j++)	{
					if(excludeList.contains(j) && j!= 50 && j!=2)	{
//						dataMatrix.setEntry(i-1, j, -1);			//This value should never be accessed as it is excluded
						continue;
					}
					System.out.println(i-1+ ","+ j+ ","+ dataLine[j]);
					dataMatrix.setEntry(i-1, j, Double.parseDouble(dataLine[j]));				
				}
				
			}
		}
		
		return dataMatrix;
	}

}