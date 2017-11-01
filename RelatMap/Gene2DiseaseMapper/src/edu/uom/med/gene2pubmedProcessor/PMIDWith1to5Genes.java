package edu.uom.med.gene2pubmedProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Program to retrieve PubMed ID mapping to up to 5 genes, according to gene2pubmed resource.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class PMIDWith1to5Genes {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="";
		
		String arg1 = args[0]; //input file
		String arg2 = args[1]; //output file
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(Integer.parseInt(arrLine[1]) >= 1 && Integer.parseInt(arrLine[1]) <= 5) {
					bw.append(arrLine[0]);
					bw.append("\n");
				}
			}
			
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Execution time (in milliseconds): "+elapsedTime);
	}
	
}
