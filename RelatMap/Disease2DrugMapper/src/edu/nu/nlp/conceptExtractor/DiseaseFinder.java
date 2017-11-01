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
	 * PHYS (stands for physiology), ACTI (stands for activity), CHEM (stands for chemicals), and PROC (stands
	 * for procedures) from UMLS Metathesaurus. Complex traits (e.g. HDL cholesterol, triglycerides) are grouped
	 * under the UMLS semantic types CHEM rather than DISO. Therefore, we use these semantic types to define 
	 * certain complex traits.
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
			if(!q.umlsSemGrps.get(i).contains("DISO") &&
					!q.umlsSemGrps.get(i).contains("PHYS") &&
					!q.umlsSemGrps.get(i).contains("ACTI") &&
					!q.umlsSemGrps.get(i).contains("CHEM") &&
					!q.umlsSemGrps.get(i).contains("PROC")) 
				continue;
		
			if(q.eligibilities.get(i)) {
				diseaseInfo = q.mentions.get(i)+"\t"+q.umlsCodes.get(i);
				if(!diseases.contains(diseaseInfo)) {
					diseases.add(diseaseInfo);
				}
			}
		}
		
		return diseases;
	}

}
