package edu.uom.med.diseaseConceptMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.mayo.nlp.qa.DiseaseAnswerer;
import edu.nu.nlp.conceptExtractor.DiseaseFinder;

/**
 * Project is meant to map the occurrence of disease mentions in sentences from PubMed articles. It uses a cutomized 
 * disease lexicon previously compiled from UMLS Metathesaurus (please refer UMLSMetathesaurusCompiler project). The
 * project uses MedTagger from Mayo clinic for mapping. It filters sentences mapped with only one disease. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DiseaseMapper {
	
	//global attributes
	static DiseaseFinder diseaseFinder = new DiseaseFinder();
	
	static DiseaseAnswerer diseaseAnswerer = new DiseaseAnswerer("resources/lookup/DiseasesLexicon");
	static ArrayList<String> diseasesList = new ArrayList<String>();
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="", line1="";
		int count=0;
		
		ArrayList<String> allSentences = new ArrayList<String>();
		
		String arg1 = args[0]; //INPUT_FILE -- PMID_sentences.txt
		String arg2 = args[1]; //OUTPUT_FILE
		
		try {
			FileReader fr01 = new FileReader("resources/lookup/DiseasesList.txt"); 
			BufferedReader br01 = new BufferedReader(fr01);
			while((line = br01.readLine())!=null) { diseasesList.add(line); }
						
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line1 = br.readLine()) != null) {
				allSentences.add(line1);
			}
			
			for(String eachSentence : allSentences) {
				
				String[] arrLine1 = eachSentence.split("\t");
				if(arrLine1.length>1) {
					String sentence = arrLine1[1];
					
					ArrayList<String> tempdiseases = diseaseFinder.getDisease(sentence, diseaseAnswerer);
					
					ArrayList<String> diseases = new ArrayList<String>();
					ArrayList<String> diseasesCUI_TUI = new ArrayList<String>();
					for(String each : tempdiseases) {
						String[] arrEach = each.split("\t");
						if(diseasesList.contains(arrEach[0])) { //(arrEach[0].toLowerCase())) {  
							if(!diseasesCUI_TUI.contains(arrEach[1])) {
								diseasesCUI_TUI.add(arrEach[1].trim());
								diseases.add(arrEach[0].trim());
							}
						}
					}
				
					if(!diseases.isEmpty()) {
						if(diseases.size()==1 && diseasesCUI_TUI.size()==1) {
							//skip abbreviations in the dictionary
							if(diseases.get(0).matches("^[A-Z0-9]") == true) continue;
									
							bw.append(eachSentence+"\t"+diseases.get(0)+"\t"+diseasesCUI_TUI.get(0));
							bw.append("\n");
						}	
					}
				}
				
				count++;
				if(count%50000==0) System.out.println(count);
			}
			
			br01.close();
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		System.out.println(count+" sentences were processed in "+duration+" milliseconds!");	
	}
	
}