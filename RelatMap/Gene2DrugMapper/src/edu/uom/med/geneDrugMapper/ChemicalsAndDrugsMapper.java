package edu.uom.med.geneDrugMapper;


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
	
	//global attribute
	static InterventionFinder interventionFinder = new InterventionFinder();
	
	/**
	 * Program execution starts from here
	 * 
	 * @param args
	 */
	
	public String performChemicalsAndDrugsMapping(String sentence, ArrayList<String> chemicalsList, 
			ChemicalAnswerer chemicalAnswerer) {
		String drugInfo="";
		
		sentence = sentence.trim();
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
					
		String drug="", drugID="";
		if(!drugs.isEmpty()) {
			if(drugsID.size() == 1) {
				for(String eachDrug : drugs) {
					if(drug.isEmpty()) drug = eachDrug;
					else drug = drug+" | "+eachDrug;
				}
				drugID = drugsID.get(0);
				drugInfo = drug+"\t"+drugID;
				
				return drugInfo;
			}
			else {
				return "";
			}
		}
		else {
			return "";
		}
	}
	
}
