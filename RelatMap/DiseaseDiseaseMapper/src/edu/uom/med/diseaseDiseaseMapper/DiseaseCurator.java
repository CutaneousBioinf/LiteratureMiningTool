package edu.uom.med.diseaseDiseaseMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Program collects a list of disease names and their synonyms from the disease lexicon and writes the output to a file. 
 * This file is used in disease-disease mapping using MedTagger from Mayo clinic.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DiseaseCurator {
	
	//global attribute
	static ArrayList<String> diseasesList = new ArrayList<String>();
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="";
		
		try {
			FileReader fr = new FileReader("resources/lookup/DiseasesLexicon");
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter("resources/lookup/DiseasesList.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			
			while((line = br.readLine())!=null) {
				String[] arrLine = line.split("\\|");
				if(arrLine[1].contains(",")) {
					arrLine[1] = arrLine[1].replaceAll(",", "");
				}
				else if(arrLine[1].contains("-")) {
					arrLine[1] = arrLine[1].replaceAll("-", " ");
				}
				
				if(!diseasesList.contains(arrLine[1])) {
					diseasesList.add(arrLine[1]); 
				}
				System.out.println(arrLine[1]);
			}
			
			for(String each : diseasesList) {
				bw.append(each);
				bw.append("\n");
			}
			
			System.out.println("Size: "+diseasesList.size());
			
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
}
