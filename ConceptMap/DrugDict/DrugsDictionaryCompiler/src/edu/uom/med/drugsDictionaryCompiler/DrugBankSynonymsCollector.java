package edu.uom.med.drugsDictionaryCompiler;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * Chemicals/Drugs lexicon is generated from three resources namely UMLS Metathesaurus, DrugBank and PharmGKB. Among these, 
 * UMLS Metathesaurus is considered as the major resource, because it includes concepts from nearly 200 resources. However, we
 * observed that DrugBank and PharmGKB includes drugs/synonyms that are not present in UMLS Metathesaurus. Therefore, we 
 * designed an approach to execute in three steps: (1) UMLS Metathesaurus is considered as the starting resource. For each drug
 * from UMLS Metathesaurus, we collected all the synonyms. Then we explored synonyms for the same drug from DrugBank and
 * PharmGKB. We combined the synonyms from DrugBank and PharmGKB that are not present in UMLS Metathesaurus; (2) DrugBank is
 * considered as the starting resource. For each drug from DrugBank (that are not in UMLS Metathesaurus), we collected all the
 * synonyms. Then we explored synonyms for the same drug in PharmGKB and combined the ones that are not present in DrugBank; 
 * (3) PharmGKB is considered as the starting resource. For each drug from PharmGKB (that are not in UMLS Metathesaurus and
 * DrugBank), we collected all the synonyms. Thus, chemicals/drugs and their synonyms from all the resources are grouped 
 * together. 
 * 
 * Matching the synonyms across the resources (i.e. UMLS Metathesaurus vs. DrugBank vs. PharmGKB) is challenging due to the 
 * morphological variations in the drug/synonym names across the resources. We addressed this challenge by carrying out the
 * matching in two steps: (1) direct string matching, where synonyms from DrugBank and PharmGKB are expected to match exactly
 * with the same synonym from UMLS Metathesaurus; and (2) normalized string matching, where we apply a set of normalization 
 * rules to attain uniform synonym name across various resources. Since the lexicon is generated from three resources, we
 * also automatically generated the customized drug ID for each drug and their synonyms.  
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugBankSynonymsCollector {
	
	/**
	 * Program identifies and collects the synonyms from DrugBank that are not available in UMLS Metathesaurus
	 * 
	 * @param synonyms
	 * @param drugBankSynonyms
	 * @param normalizedDrugBankSynonyms
	 * @param shortNameDrugs
	 * @return
	 */
	
	public DrugBankPair getSynonyms(ArrayList<String> synonyms, ArrayList<String> drugBankSynonyms, 
			ArrayList<String> normalizedDrugBankSynonyms, ArrayList<String> shortNameDrugs) {
		String drugBankID1="", drugBankID2="";
		ArrayList<String> synonyms1 = new ArrayList<String>(); //DrugBank synonyms
		ArrayList<String> drugBankID = new ArrayList<String>();
		
		//convert to lowercase for better matching
		ArrayList<String> synonymsLowerCase = new ArrayList<String>();
		for(String eachSynonyms : synonyms) {
			synonymsLowerCase.add(eachSynonyms.trim().toLowerCase());
		}
		
		ArrayList<String> drugBankSynonymsLowerCase = new ArrayList<String>();
		for(String eachDrugBankSynonyms : drugBankSynonyms) {
			drugBankSynonymsLowerCase.add(eachDrugBankSynonyms.trim().toLowerCase());
		}
		ArrayList<String> normalizedDrugBankSynonymsLowerCase = new ArrayList<String>();
		for(String eachNormalizedDrugBankSynonyms : normalizedDrugBankSynonyms) {
			normalizedDrugBankSynonymsLowerCase.add(eachNormalizedDrugBankSynonyms.trim().toLowerCase());
		}
		
		for(int i=0; i<synonymsLowerCase.size(); i++) {
			String eachSynonym = synonymsLowerCase.get(i);
			
			//to skip synonyms that are abbreviations
			/*if(eachSynonym.length()<=4) {
				if(!shortNameDrugs.contains(eachSynonym)) continue;
			}*/
			
			//normalize drug synonyms for better matching
			String eachSynonymNormalized="";
			if(eachSynonym.contains("-")) {
				eachSynonymNormalized = eachSynonym.replaceAll("-", " ");
				eachSynonymNormalized = eachSynonymNormalized.replaceAll(" ", "");
				eachSynonymNormalized = eachSynonymNormalized.trim();
			}
			else eachSynonymNormalized = eachSynonym;
			
			//to skip synonyms that are abbreviations
			/*if(eachSynonymNormalized.length()<=4) {
				if(!shortNameDrugs.contains(eachSynonymNormalized)) continue;
			}*/
			
			//direct matching
			ArrayList<String> eachDrugBankSynonymList = new ArrayList<String>();
			for(int j=0; j<drugBankSynonymsLowerCase.size(); j++) {
				String eachDrugBankSynonym = drugBankSynonymsLowerCase.get(j);
				
				if(eachDrugBankSynonym.contains("###"+eachSynonym+"###") ||
						eachDrugBankSynonym.endsWith("###"+eachSynonym)) { //may include new synonyms
					String match = drugBankSynonyms.get(j);
					String[] arrDrugBankSynonyms = match.split("###");
					eachDrugBankSynonymList = new ArrayList<String>(Arrays.asList(arrDrugBankSynonyms));
					
					drugBankID1 = arrDrugBankSynonyms[0];
					
					for(int k=1; k<arrDrugBankSynonyms.length; k++) {
						if(/*!synonyms.contains(arrDrugBankSynonyms[k]) &&*/ //--don't delete; required to have non-duplicate list of synonyms between resources
								!synonyms1.contains(arrDrugBankSynonyms[k])) {
							synonyms1.add(arrDrugBankSynonyms[k]);
						}
					}
					
					if(!drugBankID.contains(drugBankID1)) drugBankID.add(drugBankID1);
					//break;
				}
			}
			
			//normalized matching
			for(int j=0; j<normalizedDrugBankSynonymsLowerCase.size(); j++) {
				String eachDrugBankSynonym = normalizedDrugBankSynonymsLowerCase.get(j);
				
				if(!eachDrugBankSynonymList.isEmpty() && !eachSynonymNormalized.isEmpty()) {
					if(eachDrugBankSynonym.contains("###"+eachSynonymNormalized+"###") ||
							eachDrugBankSynonym.endsWith("###"+eachSynonymNormalized)) { //may include new synonyms
						
						String match = normalizedDrugBankSynonyms.get(j);
						String[] arrDrugBankSynonyms = match.split("###");
					
						drugBankID2 = arrDrugBankSynonyms[0];
						
						if(drugBankID1.equals(drugBankID2)) {
							for(int k=1; k<arrDrugBankSynonyms.length; k++) {
								if(arrDrugBankSynonyms[k].equals(eachSynonymNormalized)) {
									if(/*!synonyms.contains(eachDrugBankSynonymList.get(k)) &&*/
											!synonyms1.contains(eachDrugBankSynonymList.get(k))) {
										synonyms1.add(eachDrugBankSynonymList.get(k));
									}
								}
							}
							if(!drugBankID.contains(drugBankID2)) drugBankID.add(drugBankID2);
						}
						//break;
					}
				}
				
				if(eachDrugBankSynonymList.isEmpty() && !eachSynonymNormalized.isEmpty()) { //none of the synonyms match exactly
					if(eachDrugBankSynonym.contains("###"+eachSynonymNormalized+"###") || 
							eachDrugBankSynonym.endsWith("###"+eachSynonymNormalized)) { //may include new synonyms
						String match = normalizedDrugBankSynonyms.get(j);
						String[] arrDrugBankSynonyms = match.split("###");
					
						drugBankID2 = arrDrugBankSynonyms[0];
						
						if(drugBankID1.isEmpty()) {
							for(int k=1; k<arrDrugBankSynonyms.length; k++) {
								if(arrDrugBankSynonyms[k].equalsIgnoreCase(eachSynonymNormalized)) {
									for(String synonym : drugBankSynonyms) {
										if(synonym.startsWith(drugBankID2)) {
											String[] arrSynonym = synonym.split("###");
											for(int s=1; s<arrSynonym.length; s++) {
												if(!synonyms1.contains(arrSynonym[s])) {
													synonyms1.add(arrSynonym[s]);
												}
											}
											//break;
										}
									}
								}
							}
							if(!drugBankID.contains(drugBankID2)) drugBankID.add(drugBankID2);
						} 
						//break;
					}
				}
				
				if(!eachSynonymNormalized.isEmpty()) { //none of the synonyms match exactly
					if(eachDrugBankSynonym.contains("###"+eachSynonymNormalized+"###") || 
							eachDrugBankSynonym.endsWith("###"+eachSynonymNormalized)) { //may include new synonyms
						String match = normalizedDrugBankSynonyms.get(j);
						String[] arrDrugBankSynonyms = match.split("###");
					
						drugBankID2 = arrDrugBankSynonyms[0];
						
						for(int k=1; k<arrDrugBankSynonyms.length; k++) {
							if(arrDrugBankSynonyms[k].equalsIgnoreCase(eachSynonymNormalized)) {
								for(String synonym : drugBankSynonyms) {
									if(synonym.startsWith(drugBankID2)) {
										String[] arrSynonym = synonym.split("###");
										for(int s=1; s<arrSynonym.length; s++) {
											if(!synonyms1.contains(arrSynonym[s])) {
												synonyms1.add(arrSynonym[s]);
											}
										}
										//break;
									}
								}
							}
						}
						if(!drugBankID.contains(drugBankID2)) drugBankID.add(drugBankID2);
						//break
					}
				}
			}	
		}
		
		DrugBankPair drugBankPair = new DrugBankPair(drugBankID, synonyms1);
		
		return drugBankPair;
	}
	
	/**
	 * Class DrugBankPair returns two values. We adopted this from C++ programming language.
	 * 
	 * @author Kalpana Raja
	 *
	 */
	
	public static class DrugBankPair {
		private ArrayList<String> drugBankID;
		private ArrayList<String> synonyms1;
		DrugBankPair(ArrayList<String> d,ArrayList<String> s) {
			drugBankID = d;
			synonyms1 = s;
		}
		public ArrayList<String> getDrugBankID() {
			return drugBankID;
		}
		public ArrayList<String> getDrugBankSynonyms() {
			return synonyms1;
		}
	}
	
}
