package edu.uom.med.drugsDictionaryCompiler;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

public class UMLSSynonymsCollector {
	
	//global attributes
	static ArrayList<String> eachDrug = new ArrayList<String>(); //UMLS
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 * @throws IOException
	 */
	
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //input file --UMLSChemicalsAndDrugsSynonyms
		String arg2 = args[1]; //output file --UMLSChemicalsAndDrugsSynonyms_grouped

		String line="", preID="", cuiTui="";
		int count=0;
	
		FileReader fr = new FileReader(arg1);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(arg2);
		BufferedWriter bw = new BufferedWriter(fw);
		while((line = br.readLine()) != null) {
			String[] arrLine = line.split("\t");
			
			if(preID=="") { 
				preID = arrLine[0];
				eachDrug.add(arrLine[0]+"\t"+arrLine[1]);
			}
			else {
				if(arrLine[0].equals(preID)) {
					eachDrug.add(arrLine[0]+"\t"+arrLine[1]); //synonyms
					//cuiTui=arrLine[0];
				}
				else {
					//get all synonyms from UMLS Metathesaurus
					cuiTui="";
					String synonyms = "";
					for(String each : eachDrug) {
						String[] arrEach = each.split("\t");
						if(synonyms.isEmpty()) {
							cuiTui = arrEach[0];
							synonyms = arrEach[1]; 
						}
						else {  
							synonyms = synonyms+"###"+arrEach[1];
						}
					}
					
					//write to file
					bw.append(cuiTui+"###"+synonyms);
					bw.append("\n");
					
					preID=arrLine[0];
					cuiTui="";
					eachDrug.clear();
					eachDrug.add(arrLine[0]+"\t"+arrLine[1]);
				}
			}
			
			count++;
			//if(count==10000) break;
		}
		
		//last record
		//get all synonyms from UMLS Metathesaurus
		cuiTui="";
		String synonyms = "";
		for(String each : eachDrug) {
			String[] arrEach = each.split("\t");
			if(synonyms.isEmpty()) {
				cuiTui = arrEach[0];
				synonyms = arrEach[1]; 
			}
			else {  
				synonyms = synonyms+"###"+arrEach[1];
			}
		}
		
		//write to file
		bw.append(cuiTui+"###"+synonyms);
		bw.append("\n");
		
		br.close();
		bw.close();
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
}
