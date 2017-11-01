package edu.uom.med.drugGeneAndPathway;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Program to count pathways from CGAP and Reactome
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class PathwaysCounter {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //DRUG_GENE_PATHWAY (output from DrugGenePathwayCompiler.java) 

		String line="";
		
		Set<String> pathways = new HashSet<String>();
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			while((line=br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				if(arrLine[2].contains("|")) {
					String[] pathwaylist = arrLine[2].split("\\|");
					for(int i=0; i<pathwaylist.length; i++) {
						pathways.add(pathwaylist[i]);
					}
				}
				else {
					pathways.add(arrLine[2]);
				}
			}
			
			System.out.println("Number of pathways: "+ pathways.size());
			
			br.close();
		} catch(IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
    	long elapsedTime = stopTime - startTime;
    	System.out.println("Execution time in milliseconds: " + elapsedTime);	
	}
	
}
