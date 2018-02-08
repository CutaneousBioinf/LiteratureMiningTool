package edu.uom.med.conceptsDictionaryGenerator;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/** 
 * The MRCONSOPreferredVocabularyGenerator program implements an application that extracts 
 * concepts in English that are labeled preferred from the MRCONSO.RRF file within 
 * UMLS Metathesaurus.  
 * 
 * @author Kalpana Raja and Troy Cao
 *
 */

public class MRCONSOPreferredVocabularyGenerator {
	
	/**
	 * Method reads and filters the concepts from MRCONSO.RRF
	 * 
	 * @throws IOException
	 */
	public void conceptsAndSynonymsExtractor(String mrconsoFile) throws IOException {
		
		System.out.println("Processing MRCONSO within UMLS Metathesaurus....");
		
		//To read MRCONSO.RRF within UMLS Metathesaurus 2016AA version   
		BufferedReader in = new BufferedReader(new FileReader(mrconsoFile));
		PrintWriter pwr = new PrintWriter("resources/secondaryDictionaries/mrconso.preferredVocabularies2016AA");
		
		int count=0, count1=0;
		while(in.ready()){
			String line = in.readLine();
			String[] arrLine = line.split("\\|");
			if(arrLine[1].trim().equals("ENG") && arrLine[6].equals("Y")) {
				pwr.append(line);
				pwr.append("\n");
				count1++;
			}
			
			count++;
			if(count%1000==0) System.out.println("Retrieved " + count + " concepts so far...");
		}
		
		System.out.println("\n\nThe number of concepts: "+count1);
		in.close();
		pwr.close();
	}
	
}
