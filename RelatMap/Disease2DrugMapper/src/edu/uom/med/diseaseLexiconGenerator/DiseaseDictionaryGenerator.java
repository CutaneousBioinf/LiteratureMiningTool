package edu.uom.med.diseaseLexiconGenerator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Program to separate disease concepts from UMLS Metathesaurus.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DiseaseDictionaryGenerator {
	
	/**
	 * Program execution starts here.
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		String line="";
		int count=0;
		
		//generate dictionary
		try {
			FileReader fr = new FileReader("resources/secondaryDictionaries/umls.conceptsVocabulary2015AB");
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter("resources/secondaryDictionaries/mrconso.diseaseVocabularies.diseasesOnly.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine())!=null) {
				//disease related semantic types
				if(line.trim().contains("T020") || 			//Acquired Abnormality
						line.trim().contains("T190") || 	//Anatomical Abnormality
						line.trim().contains("T049") || 	//Cell or Molecular Dysfunction
						line.trim().contains("T019") ||		//Congenital Abnormality
						line.trim().contains("T047") || 	//Disease or Syndrome
						line.trim().contains("T050") || 	//Experimental Model of Disease
						line.trim().contains("T033") || 	//Finding
						line.trim().contains("T037") || 	//Injury or Poisoning
						line.trim().contains("T048") ||		//Mental or Behavioral Dysfunction
						line.trim().contains("T191") ||		//Neoplastic Process
						line.trim().contains("T046") || 	//Pathologic Function
						line.trim().contains("T184")) { 	//Sign or Symptom
					bw.append(line);
					bw.append("\n");
					count++;
				}
				
			}
			
			System.out.println("Number of disease concepts (inlcuding synonyms): " + count);
			
			br.close();
			bw.close();
		} catch (IOException ie) {
			System.err.println(ie);
		}
	}
	
}
