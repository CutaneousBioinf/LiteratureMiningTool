package edu.nu.nlp.conceptExtractor;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.mayo.nlp.qa.ChemicalAnswerer;
import edu.mayo.nlp.qa.Question;

/**
 * The program maps the chemicals, drugs or their synonyms present in sentences from PubMed abstracts. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class InterventionFinder {
	
	/**
	 * Method identifies chemicals, drugs and synonyms based on UMLS semantic type CHEM
	 * from UMLS Metathesaurus
	 * 
	 * @param sent
	 * @param a
	 * @return
	 */
	
	public ArrayList<String> getIntervention(String sent, ChemicalAnswerer a) {		
		String drugInfo="", chemicalAndDrug="";
		ArrayList<String> drugs = new ArrayList<String>();
		
		Question q = new Question(sent, true, false);
		a.fetchMentions(q);
		
		for(int i=0; i< q.mentions.size(); i++) {
			if(!q.umlsSemGrps.get(i).contains("CHEM"))
				continue;
			
			if(q.eligibilities.get(i)) {
				chemicalAndDrug = q.mentions.get(i);
				if(chemicalAndDrug.length()<=4) {
					Pattern pattern = Pattern.compile("^[A-Z]+$");
					Matcher matcher = pattern.matcher(chemicalAndDrug);
					if(matcher.find()) {
						continue;
					}
				}
				
				drugInfo = q.mentions.get(i)+"\t"+q.umlsCodes.get(i);
				if(!drugs.contains(drugInfo)) {
					drugs.add(drugInfo); 
				}	
			}
		}
		
		return drugs;
	}
	
}

