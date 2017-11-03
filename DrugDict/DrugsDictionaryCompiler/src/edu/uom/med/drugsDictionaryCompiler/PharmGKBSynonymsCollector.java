package edu.uom.med.drugsDictionaryCompiler;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * Program collects synonyms (for a drug) from PharmGKB that are not present in UMLS Metathesaurus and DrugBank (for the same 
 * drug). Each synonym from PharmGKB is matched with the list of synonyms from UMLS Metathesaurus and DrugBank to confirm that  
 * they are not present.This matching is done in two steps. First, we carried out a direct string matching in which a synonym 
 * from PharmGKB and UMLS Metathesaurus / DrugBank is expected to the same. However, this can't be true for all synonyms, 
 * because different databases may represent the same synonym with variations (e.g. case could be different; presence of hyphen). 
 * To address this problem, we came up with a set of normalization rules to convert drug synonyms to a base form and then 
 * carried out the string matching between these base forms.  
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class PharmGKBSynonymsCollector {
	
	/**
	 * Program identifies and collects the synonyms from PharmGKB that are not available in UMLS Metathesaurus and DrugBank
	 * 
	 * @param synonyms
	 * @param synonyms1
	 * @param pharmGKBSynonyms
	 * @param normalizedPharmGKBSynonyms
	 * @param shortNameDrugs
	 * @return
	 */
	
	public PharmGKBPair getSynonyms(ArrayList<String> synonyms, ArrayList<String> synonyms1,
			ArrayList<String> pharmGKBSynonyms, ArrayList<String> normalizedPharmGKBSynonyms,
			ArrayList<String> shortNameDrugs) {
		String pharmGKBID1="", pharmGKBID2="";
		ArrayList<String> pharmGKBID = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
		temp.addAll(synonyms); //UMLS synonyms
		temp.addAll(synonyms1); //DrugBank synonyms
		
		ArrayList<String> synonyms2 = new ArrayList<String>(); //copy synonyms to here
		
		//convert to lowercase for better matching
		ArrayList<String> tempLowerCase = new ArrayList<String>();
		for(String eachTemp : temp) {
			tempLowerCase.add(eachTemp.trim().toLowerCase());
		}
		
		ArrayList<String> pharmGKBSynonymsLowerCase = new ArrayList<String>();
		for(String eachPharmGKBSynonyms : pharmGKBSynonyms) {
			pharmGKBSynonymsLowerCase.add(eachPharmGKBSynonyms.trim().toLowerCase());
		}
		ArrayList<String> normalizedPharmGKBSynonymsLowerCase = new ArrayList<String>();
		for(String eachNormalizedPharmGKBSynonyms : normalizedPharmGKBSynonyms) {
			normalizedPharmGKBSynonymsLowerCase.add(eachNormalizedPharmGKBSynonyms.trim().toLowerCase());
		}
		
		for(int i=0; i<tempLowerCase.size(); i++) {
			//eachSynonym = eachSynonym.toLowerCase();
			String eachSynonym = tempLowerCase.get(i);
			
			//to skip synonyms that are abbreviations
			/*if(eachSynonym.length()<=4) {
				if(!shortNameDrugs.contains(eachSynonym)) continue;
			}*/
			
			//normalize drug synonyms for better matching
			String eachSynonymNormalized="";
			if(eachSynonym.contains("-")) {
				eachSynonymNormalized = eachSynonym.replaceAll("-", " ");
				eachSynonymNormalized = eachSynonymNormalized.replaceAll(" ", "");
			}
			else eachSynonymNormalized = eachSynonym;
			
			//to skip synonyms that are abbreviations
			/*if(eachSynonymNormalized.length()<=4) {
				if(!shortNameDrugs.contains(eachSynonymNormalized)) continue;
			}*/
			
			//direct matching
			ArrayList<String> eachPharmGKBSynonymList = new ArrayList<String>();
			for(int j=0; j<pharmGKBSynonymsLowerCase.size(); j++) {
				String eachPharmGKBSynonym = pharmGKBSynonymsLowerCase.get(j);
				
				if(eachPharmGKBSynonym.contains("###"+eachSynonym+"###") ||
						eachPharmGKBSynonym.endsWith("###"+eachSynonym)) { //may include new synonyms
					String match = pharmGKBSynonyms.get(j);
					String[] arrPharmGKBSynonyms = match.split("###");
					eachPharmGKBSynonymList = new ArrayList<String>(Arrays.asList(arrPharmGKBSynonyms));
					
					pharmGKBID1 = arrPharmGKBSynonyms[0];
					for(int k=1; k<arrPharmGKBSynonyms.length; k++) {
						if(/*!temp.contains(arrPharmGKBSynonyms[k]) && */
								!synonyms2.contains(arrPharmGKBSynonyms[k])) {
							synonyms2.add(arrPharmGKBSynonyms[k]);
						}
					}
					if(!pharmGKBID.contains(pharmGKBID1)) pharmGKBID.add(pharmGKBID1);
					//break;
				}
			}
			
			//normalized matching
			for(int j=0; j<normalizedPharmGKBSynonymsLowerCase.size(); j++) {
				String eachPharmGKBSynonym = normalizedPharmGKBSynonymsLowerCase.get(j);
				
				if(!eachPharmGKBSynonymList.isEmpty() && !eachSynonymNormalized.isEmpty()) {
					if(eachPharmGKBSynonym.contains("###"+eachSynonymNormalized+"###") ||
							eachPharmGKBSynonym.endsWith("###"+eachSynonymNormalized)) { //may include new synonyms
						String match = normalizedPharmGKBSynonyms.get(j);
						String[] arrPharmGKBSynonyms = match.split("###");
					
						pharmGKBID2 = arrPharmGKBSynonyms[0];
						if(pharmGKBID1.equals(pharmGKBID2)) {
							for(int k=1; k<arrPharmGKBSynonyms.length; k++) {
								if(arrPharmGKBSynonyms[k].equals(eachSynonymNormalized)) {
									if(/*!temp.contains(eachPharmGKBSynonymList.get(k)) && */
											!synonyms2.contains(eachPharmGKBSynonymList.get(k))) {
										synonyms2.add(eachPharmGKBSynonymList.get(k));
									}
								}
							}
							if(!pharmGKBID.contains(pharmGKBID2)) pharmGKBID.add(pharmGKBID2);
						}
						//break;
					}
					
					if(eachPharmGKBSynonymList.isEmpty() && !eachSynonymNormalized.isEmpty()) { //none of the synonyms match exactly
						if(eachPharmGKBSynonym.contains("###"+eachSynonymNormalized+"###") ||
								eachPharmGKBSynonym.endsWith("###"+eachSynonymNormalized)) { //may include new synonyms
							String match = normalizedPharmGKBSynonyms.get(j);
							String[] arrDrugBankSynonyms = match.split("###");
						
							pharmGKBID2 = arrDrugBankSynonyms[0];
							
							if(pharmGKBID1.isEmpty()) {
								for(int k=1; k<arrDrugBankSynonyms.length; k++) {
									
									if(arrDrugBankSynonyms[k].equalsIgnoreCase(eachSynonymNormalized)) {
										
										for(String synonym : pharmGKBSynonyms) {
											if(synonym.startsWith(pharmGKBID2)) {
												String[] arrSynonym = synonym.split("###");
												for(int s=1; s<arrSynonym.length; s++) {
													if(!synonyms2.contains(arrSynonym[s])) {
														synonyms2.add(arrSynonym[s]);
													}
												}
												//break;
											}
										}
									}
								}
								if(!pharmGKBID.contains(pharmGKBID2)) pharmGKBID.add(pharmGKBID2);
							}
							//break;
						}
					}
					
					if(!eachSynonymNormalized.isEmpty()) { //none of the synonyms match exactly
						if(eachPharmGKBSynonym.contains("###"+eachSynonymNormalized+"###") || 
								eachPharmGKBSynonym.endsWith("###"+eachSynonymNormalized)) { //may include new synonyms
							String match = normalizedPharmGKBSynonyms.get(j);
							String[] arrDrugBankSynonyms = match.split("###");
						
							pharmGKBID2 = arrDrugBankSynonyms[0];
							
							for(int k=1; k<arrDrugBankSynonyms.length; k++) {
								if(arrDrugBankSynonyms[k].equalsIgnoreCase(eachSynonymNormalized)) {
									for(String synonym : pharmGKBSynonyms) {
										if(synonym.startsWith(pharmGKBID2)) {
											String[] arrSynonym = synonym.split("###");
											for(int s=1; s<arrSynonym.length; s++) {
												if(!synonyms2.contains(arrSynonym[s])) {
													synonyms2.add(arrSynonym[s]);
												}
											}
											//break;
										}
									}
								}
							}
							if(!pharmGKBID.contains(pharmGKBID2)) pharmGKBID.add(pharmGKBID2);
							//break
						}
					}
				}
			}
		}
		
		PharmGKBPair pharmGKBPair = new PharmGKBPair(pharmGKBID, synonyms2);
		
		return pharmGKBPair; 
	}
	
	/**
	 * Class DrugBankPair returns two values. We adopted this from C++ programming language.
	 * 
	 * @author Kalpana Raja
	 *
	 */
	
	public static class PharmGKBPair {
		private ArrayList<String> pharmGKBID;
		private ArrayList<String> synonyms2;
		PharmGKBPair(ArrayList<String> p,ArrayList<String> s) {
			pharmGKBID = p;
			synonyms2 = s;
		}
		public ArrayList<String> getPharmGKBID() {
			return pharmGKBID;
		}
		public ArrayList<String> getPharmGKBSynonyms() {
			return synonyms2;
		}
	}
	
}

