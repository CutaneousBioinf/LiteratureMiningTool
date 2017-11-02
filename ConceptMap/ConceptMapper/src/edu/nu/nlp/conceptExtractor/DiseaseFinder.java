package edu.nu.nlp.conceptExtractor;


import java.util.ArrayList;

import edu.mayo.nlp.qa.DiseaseAnswerer;
import edu.mayo.nlp.qa.Question;

/**
 * The program maps the complex disease mentions and their synonyms present in sentences from PubMed abstracts. 
 *
 *
 * @author Kalpana Raja
 *
 */

public class DiseaseFinder {
	
	/**
	 * Method identifies disease mentions and synonyms based on UMLS semantic type DISO (stands for diseases),
	 * 
	 * 
	 * @param text
	 * @param a
	 * @return
	 */
	
	public ArrayList<String> getDisease(String text, DiseaseAnswerer a) {
		String diseaseInfo="";
		ArrayList<String> diseases = new ArrayList<String>();
		Question q = new Question(text, true, false);
		
		a.fetchMentions(q);
		
		for(int i=0; i< q.mentions.size(); i++) {
			if(!q.umlsSemGrps.get(i).contains("DISO")) 
				continue;
		
			if(q.eligibilities.get(i)) {
				diseaseInfo = q.mentions.get(i)+"\t"+q.umlsCodes.get(i);
				if(q.mentions.get(i).equals("type 2 diabetes")) {
					diseaseInfo = q.mentions.get(i)+"\tC0011860_T047";
				}
				
				if(!diseases.contains(diseaseInfo)) {
					diseases.add(diseaseInfo);
				}
			}
		}
		
		return diseases;
	}
	
}
