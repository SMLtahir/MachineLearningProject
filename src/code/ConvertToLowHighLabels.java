package code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class ConvertToLowHighLabels {

	private static final String OLD_DATA_PATH = "src/resources/movies_and_scores.tsv";
	private static final String NEW_DATA_PATH = "src/resources/movies_and_scores_new.tsv";
	
	public static void main(String[] args) throws IOException {

		List<String> oldDataLines = FileUtils.readLines(new File(OLD_DATA_PATH));
		HashMap<String, Double> yearlyAvgSales = new HashMap<String, Double>();
		String oldYear = "";
		double totalSales = 0;
		int count = 1;
		boolean isHeaderCrossed = false;
		
		for(String oldLine : oldDataLines)	{
			if(isHeaderCrossed == false && oldLine.contains("id"))	{	
				isHeaderCrossed = true;
				continue;
			}
			String[] splitLine = oldLine.split("\t");
			String year = splitLine[3];
			double sales = Double.parseDouble(splitLine[65]);
			
			if(year.equals(oldYear))	{
				totalSales += sales;
//				System.out.println("Sales "+ sales);
				count++;
			}
			else if(!year.equals(oldYear))	{
				if(!oldYear.equals(""))	{
					yearlyAvgSales.put(oldYear,totalSales/(double)count);
					System.out.println("Average Sales for "+ oldYear+ ": "+ yearlyAvgSales.get(oldYear));
				}
				totalSales = sales;
//				System.out.println("Sales "+ sales);
				count = 1;
			}
			oldYear = year;
		}
		
		//For end of file year
		yearlyAvgSales.put(oldYear,totalSales/(double)count);
		System.out.println("Average Sales for "+ oldYear+ ": "+ yearlyAvgSales.get(oldYear));
		
		writeNewDataFile(NEW_DATA_PATH, yearlyAvgSales, oldDataLines);
//		System.out.println("Average Sales for 1980: "+ yearlyAvgSales.get("1980"));
		System.out.println();
	}

	private static void writeNewDataFile(String newDataPath,
			HashMap<String, Double> yearlyAvgSales, List<String> oldDataLines) throws IOException {

		String outputString = "";
		boolean isHeaderCrossed = false;
		
		for(String oldLine : oldDataLines)	{
			if(isHeaderCrossed == false && oldLine.contains("id"))	{
				isHeaderCrossed = true;
				outputString = oldLine+ "\t"+ "classLabel"+ "\n";
				continue;
			}
			
			String[] splitLine = oldLine.split("\t");
			String year = splitLine[3];
			double sales = Double.parseDouble(splitLine[65]);
			if(sales <= yearlyAvgSales.get(year))	{
				outputString += oldLine+ "\t"+ "Low"+ "\n";
			}
			else if(sales > yearlyAvgSales.get(year))	{
				outputString += oldLine+ "\t"+ "High"+ "\n";
			}
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(newDataPath), false));
		writer.write(outputString);
		writer.close();
	}

}
