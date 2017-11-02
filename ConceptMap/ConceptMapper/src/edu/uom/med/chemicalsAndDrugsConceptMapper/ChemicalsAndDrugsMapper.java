package edu.uom.med.chemicalsAndDrugsConceptMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.mayo.nlp.qa.ChemicalAnswerer;
import edu.nu.nlp.conceptExtractor.InterventionFinder;

/**
 * Project maps chemical and drug mentions in each sentence from PubMed abstract. The project involves many 
 * preprocessing steps such as citation retrieval using EUtilites, filtration of PMIDs with up to 5 genes annotations
 * usig gene2pubmed resource from NCI and customized chemicals and drugs dictionary from three resources
 * namely UMLS Metathesaurus, DrugBank and PharmGKB. We also combine MedTagger from Mayo clinic for mapping purposes.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class ChemicalsAndDrugsMapper {
	
	//global attributes
	static InterventionFinder interventionFinder = new InterventionFinder();
	static ChemicalAnswerer chemicalAnswerer = new ChemicalAnswerer("resources/lookup/ChemicalsAndDrugsLexicon");;
	
	static ArrayList<String> chemicalsList = new ArrayList<String>();
	
	/**
	 * Program execution starts from here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();;
		
		String line="", line1="";
		int count=0, count1=0;
		
		String arg1 = args[0]; //INPUT_FILE -- PMID\tsentence format
		String arg2 = args[1]; //OUTPUT_FILE
	
		try {
			FileReader fr00 = new FileReader("resources/lookup/ChemicalsAndDrugsList.txt");
			BufferedReader br00 = new BufferedReader(fr00);
			while((line = br00.readLine())!=null) { chemicalsList.add(line); }
			
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
		
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			
			while((line1 = br.readLine()) != null) {
				String[] arrLine1 = line1.split("\t");
				
				if(arrLine1.length>1) {
					String sentence = arrLine1[1];
					
					ArrayList<String> tempinterventions = interventionFinder.getIntervention(sentence, chemicalAnswerer);
					ArrayList<String> drugs = new ArrayList<String>();
					ArrayList<String> drugsID = new ArrayList<String>();
					for(String each : tempinterventions) {
						String[] arrEach = each.split("\t");
						if(chemicalsList.contains(arrEach[0])) {
							if(sentence.contains(" "+arrEach[0]+" ") || sentence.contains(arrEach[0]+" ") ||
									sentence.endsWith(" "+arrEach[0]+".") || sentence.contains("("+arrEach[0]+")") ||
									sentence.contains("("+arrEach[0]+" ") || sentence.contains(" "+arrEach[0]+")")) {
								if(!drugs.contains(arrEach[0])) drugs.add(arrEach[0].trim());
								if(!drugsID.contains(arrEach[1])) drugsID.add(arrEach[1].trim());
							}
						}
					}
					
					String drug="";
					if(!drugs.isEmpty()) {
						if(drugsID.size() > 0) {
							for(String eachDrug : drugs) {
								if(drug.isEmpty()) drug = eachDrug;
								else drug = drug+" | "+eachDrug;
							}
							
							System.out.println(arrLine1[0] + " \t" + drug);
							
							bw.append(line1+"\t"+drug+"\t"+drugsID);
							bw.append("\n");
							count1++;
						}	
					}
				}
				
				count++;
				//if(count==10) break;
				
				//if(count1==100) break;
				//if(count%10000==0) System.out.println(count);
			}
			
			br00.close();
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		
		System.out.println(count1+" sentences were processed in "+duration);	
	}
}