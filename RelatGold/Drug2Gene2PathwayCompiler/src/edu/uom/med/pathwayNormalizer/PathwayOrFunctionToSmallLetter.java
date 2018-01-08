package edu.uom.med.pathwayNormalizer;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Program changes the pathway/function name to small letter   
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class PathwayOrFunctionToSmallLetter {

	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();	
	
		String line="";
		int count=0;

		String arg1 = args[0]; //input_file
		String arg2 = args[1]; //output_file
		
		ArrayList<String> data = new ArrayList<String>();
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			ArrayList<String> eachdata = new ArrayList<String>();
			bw.append("Gene_symbol\tPathway/Function");
			bw.append("\n");
			while((line=br.readLine()) != null) {
				String[] arrLine = line.split("\t");

				if(arrLine[1].contains(":GO")) {
					String pathway = arrLine[1].substring(0, arrLine[1].indexOf(":GO"));
					arrLine[1] = pathway.toLowerCase()+":GO";
				}
				else {
					arrLine[1] = arrLine[1].toLowerCase();
				}
 				
				bw.append(arrLine[0] + "\t" + arrLine[1]);
				bw.append("\n");
			}
			
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
	
		long stopTime = System.currentTimeMillis();
      	long elapsedTime = stopTime - startTime;
      	System.out.println("execution time in milliseconds: " + elapsedTime);
	}
	
}
