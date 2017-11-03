package edu.uom.med.drugsDictionaryGenerator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class DrugsDictionaryGenerator {
	
	//global attributes
	static StopwordsRemover stopwordsRemover = new StopwordsRemover();
	static ArrayList<String> stopwords = new ArrayList<String>();
	
	/**
	 * Constructor
	 * 
	 */
	public DrugsDictionaryGenerator() {
		String line="";
		try {
			FileReader fr = new FileReader("resources/stopwords_list.txt");
			BufferedReader br = new BufferedReader(fr);
			while((line=br.readLine()) != null) {
				stopwords.add(line);
			}
			br.close();
		}catch(IOException e) {
			System.err.println(e);
		}
	}
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 * @throws IOException
	 */
	
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		
		String line="", drugID="", umlsSynonyms="", drugBankSynonyms="", pharmGKBSynonyms="", norm="", cui_tui="", tui="";
		int count=0;
		
		String arg1 = args[0]; //INPUT_FILE -- drugsDictionary
		String arg2 = args[1]; //OUTPUT_FILE -- drugsDictionary_UMLS_DrugBank_PharmGKB.txt
		
		FileInputStream fis = new FileInputStream(arg1);
		InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		BufferedReader br = new BufferedReader(isr);
		FileOutputStream fos = new FileOutputStream(arg2);
	    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
	    BufferedWriter bw = new BufferedWriter(osr);
	    
		while((line = br.readLine()) != null) {
			String[] arrLine = line.split("\t");
			
			drugID = arrLine[0];
			umlsSynonyms = arrLine[1];
			drugBankSynonyms = arrLine[2];
			pharmGKBSynonyms = arrLine[3];
			//cui_tui = arrLine[4];
			tui = "_T000"; 
			
			//if(!cui_tui.isEmpty()) tui = cui_tui.substring(cui_tui.indexOf("_"));
			//else tui = "_T000"; //Semantic type is not available -- drug is from Drugbank or PharmGKB
			
			//process umlsSynonyms
			if(!umlsSynonyms.isEmpty()) {
				if(umlsSynonyms.contains("###")) {
					String[] arrUMLSSynonyms = umlsSynonyms.split("###");
					for(String eachUMLSSynonym : arrUMLSSynonyms) {
						//normalized form
						norm = getNormalizedDrugName(eachUMLSSynonym);
						
						bw.append(norm+"|"+eachUMLSSynonym+"||CHEM|"+drugID+tui);
						bw.append("\n");
					}
				}
				else {
					//normalized form
					norm = getNormalizedDrugName(umlsSynonyms);
					
					bw.append(norm+"|"+umlsSynonyms+"||CHEM|"+drugID+tui);
					bw.append("\n");
				}
			}
			
			//process drugBankSynonyms
			if(!drugBankSynonyms.isEmpty()) {
				if(drugBankSynonyms.contains("###")) {
					String[] arrDrugBankSynonyms = drugBankSynonyms.split("###");
					for(String eachDrugBankSynonym : arrDrugBankSynonyms) {
						//normalized form
						norm = getNormalizedDrugName(eachDrugBankSynonym);
						
						bw.append(norm+"|"+eachDrugBankSynonym+"||CHEM|"+drugID+tui);
						bw.append("\n");
					}
				}
				else {
					//normalized form
					norm = getNormalizedDrugName(drugBankSynonyms);
					
					bw.append(norm+"|"+drugBankSynonyms+"||CHEM|"+drugID+tui);
					bw.append("\n");
				}
			}
			
			//process pharmGKBSynonyms
			if(!pharmGKBSynonyms.isEmpty()) {
				if(pharmGKBSynonyms.contains("###")) {
					String[] arrPharmGKBSynonyms = pharmGKBSynonyms.split("###");
					for(String eachPharmGKBSynonym : arrPharmGKBSynonyms) {
						//normalized form
						norm = getNormalizedDrugName(eachPharmGKBSynonym);
						
						bw.append(norm+"|"+eachPharmGKBSynonym+"||CHEM|"+drugID+tui);
						bw.append("\n");
					}
				}
				else {
					//normalized form
					norm = getNormalizedDrugName(pharmGKBSynonyms);
					
					bw.append(norm+"|"+pharmGKBSynonyms+"||CHEM|"+drugID+tui);
					bw.append("\n");
				}
			}
			
			count++;
			//if(count==5) break;
			if(count%50000==0) {
				long stopTime = System.currentTimeMillis();
			    long elapsedTime = stopTime - startTime;
				System.out.println(count+" records were processed in "+elapsedTime);
			}
		}
		br.close();
		bw.close();
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Execution time in milliseconds: " + elapsedTime);
	}	
	
	/**
	 * Method to wrap the set of normalization rules applicable to drug / synonyms names prior to matching their
	 * names from various resources namely UMLS Metathesaurus, DrugBank and PharmGKB.
	 * 
	 * @param drugName
	 * @return
	 */
	
	public static String getNormalizedDrugName(String drugName) {
		String norm="";
		
		//Rule 1: Normalization the case
		drugName = drugName.toLowerCase();
		
		//Rule 2: Replacement of hyphen with space
		if(drugName.contains("-")) drugName = drugName.replaceAll("-", " ");
		
		//Rule 3: Elimination of word delimiter features (i.e. semicolon, colon and comma)
		Pattern p = Pattern.compile("[,;:!_&^=\"\']");
		Matcher m = p.matcher(drugName);
		while(m.find()) {	
			drugName = drugName.replaceAll(m.group().toString(), " ");
		}
		Pattern p1 = Pattern.compile("[\n\t\r]");
		Matcher m1 = p1.matcher(drugName);
		while(m1.find()) {	
			drugName = drugName.replaceAll(m1.group().toString(), "");
		}
		
		//Rule 4: Removal of stop words
		drugName = stopwordsRemover.removeStopwords(drugName, stopwords);
				
		//Rule 5: Removal of braces or parenthesis
		if(drugName.contains(")")) {
			drugName = drugName.replaceAll("\\(", " ");
			drugName = drugName.replaceAll("\\)", " ");
		}
		if(drugName.contains("\\]"))  {
			drugName = drugName.replaceAll("\\[", " ");
			drugName = drugName.replaceAll("\\]", " ");
		}
		
		//Rule 6: Replacement of space with tab
		if(drugName.contains(" ")) drugName = drugName.replaceAll(" ", "\t");
		
		norm = drugName.trim();
		
		return norm;
	}
	
}
