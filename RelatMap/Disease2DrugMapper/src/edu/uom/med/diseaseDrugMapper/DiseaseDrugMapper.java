package edu.uom.med.diseaseDrugMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.mayo.nlp.qa.ChemicalAnswerer;
import edu.mayo.nlp.qa.DiseaseAnswerer;
import edu.nu.nlp.conceptExtractor.DiseaseFinder;
import edu.nu.nlp.conceptExtractor.InterventionFinder;
import edu.uom.med.preProcessor.AbbreviationResolver;

/**
 * Project maps complex diseases (43 in the current study) and chemicals/drugs mentions in the sentences from
 * PubMed abstracts. The disease dictionary was created from UMLS Metathesaurus and 43 complex diseases alone were
 * filtered as a sub-dictionary. The chemicals/drugs dictionary was compiled from three resources namely UMLS
 * Metathesaurus, DrugBank and PharmGKB. Additionally, we use MedTagger from Mayo Clinic for mapping disease and
 * chemical/drug mentions in the sentences.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DiseaseDrugMapper {
	
	//global attributes
	static AbbreviationResolver abbreviationResolver = new AbbreviationResolver();
	static InterventionFinder interventionFinder = new InterventionFinder();
	static DiseaseFinder diseaseFinder = new DiseaseFinder();
	
	static ChemicalAnswerer chemicalAnswerer = new ChemicalAnswerer("resources/lookup/ChemicalsAndDrugsLexicon");;
	static DiseaseAnswerer diseaseAnswerer = new DiseaseAnswerer("resources/lookup/DiseasesLexicon");
	static ArrayList<String> chemicalsList = new ArrayList<String>();
	static ArrayList<String> diseasesList = new ArrayList<String>();
	
	/**
	 * Program execution starts from here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		String line="", line1="";
		int count=0, count1=0;
		
		ArrayList<String> allSentences = new ArrayList<String>();
		
		String arg1 = args[0]; //input file -- PMID_sentences.txt
		String arg2 = args[1]; //output file
		
		try {
			FileReader fr00 = new FileReader("resources/lookup/ChemicalsAndDrugsList.txt");
			BufferedReader br00 = new BufferedReader(fr00);
			while((line = br00.readLine())!=null) { chemicalsList.add(line); }
						
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
					
					ArrayList<String> tempdrugs = interventionFinder.getIntervention(sentence, chemicalAnswerer);
					ArrayList<String> tempdiseases = diseaseFinder.getDisease(sentence, diseaseAnswerer);
					
					ArrayList<String> drugs = new ArrayList<String>();
					ArrayList<String> drugsID = new ArrayList<String>();
					for(String each : tempdrugs) {
						String[] arrEach = each.split("\t");
						if(chemicalsList.contains(arrEach[0])) { 
							if(!drugs.contains(arrEach[0])) drugs.add(arrEach[0].trim());
							if(!drugsID.contains(arrEach[1])) drugsID.add(arrEach[1].trim());
						}
					}
				
					ArrayList<String> diseases = new ArrayList<String>();
					ArrayList<String> diseasesCUI_TUI = new ArrayList<String>();
					for(String each : tempdiseases) {
						String[] arrEach = each.split("\t");
						if(diseasesList.contains(arrEach[0])) { 
							if(!diseases.contains(arrEach[0])) diseases.add(arrEach[0].trim()); 
							if(!diseasesCUI_TUI.contains(arrEach[1])) diseasesCUI_TUI.add(arrEach[1].trim());
						}
					}
				
					String disease="", drug="";
					if(!drugs.isEmpty() && !diseases.isEmpty()) {
						if(drugsID.size()==1 && diseasesCUI_TUI.size()==1) {
							for(String eachDisease : diseases) {
								if(disease.isEmpty()) disease = eachDisease;
								else disease = disease+" | "+ eachDisease;
							}
								
							for(String eachDrug : drugs) {
								if(drug.isEmpty()) drug = eachDrug;
								else drug = drug+" | "+eachDrug;
							}
							
							if(!drug.equalsIgnoreCase(disease)) {
								bw.append(eachSentence+"\t"+disease+"\t"+drug+"\t"+diseasesCUI_TUI.get(0)+"\t"+drugsID.get(0));
								bw.append("\n");
								count1++;
							}
						}	
					}
				}
				
				count++;
				//if(count1==100) break;
				//if(count%50000==0) System.out.println(count);
			}
			
			br00.close();
			br01.close();
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		System.out.println(count1+" sentences were processed in "+duration+" milliseconds!");	
	}
	
}