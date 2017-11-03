package edu.uom.med.drugsDictionaryCompiler;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import edu.uom.med.drugsDictionaryCompiler.DrugBankSynonymsCollector.DrugBankPair;
import edu.uom.med.drugsDictionaryCompiler.PharmGKBSynonymsCollector.PharmGKBPair;

/**
 * Program is meant to test the mapping of synonyms between UMLS Metathesaurus and DrugBank + PharmGKB. This is the first step
 * of the chemicals/drugs lexicon generation. UMLS Metathesaurus is considered as the starting resource. For each drug
 * from UMLS Metathesaurus, we collected all the synonyms. Then we explored synonyms for the same drug from DrugBank and
 * PharmGKB. We combined the synonyms from DrugBank and PharmGKB that are not present in UMLS Metathesaurus.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugsDictionaryCompilerLoop1 {
	
	//global attributes
	static ArrayList<String> drugBankSynonyms = new ArrayList<String>();
	static ArrayList<String> pharmGKBSynonyms = new ArrayList<String>();
	
	static ArrayList<String> normalizedDrugBankSynonyms = new ArrayList<String>();
	static ArrayList<String> normalizedPharmGKBSynonyms = new ArrayList<String>();
	
	static DrugBankSynonymsCollector drugBankSynonymsCollector = new DrugBankSynonymsCollector();
	static PharmGKBSynonymsCollector pharmGKBSynonymsCollector = new PharmGKBSynonymsCollector();
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="", cuiTui="", drugID="";
		int count=0;
		
		ArrayList<String> shortNameDrugs = new ArrayList<String>();
		
		String arg1 = args[0]; //INPUT_FILE1 -- drugBankSynonyms
		String arg2 = args[1]; //INPUT_FILE2 -- pharmgkbSynonyms
		String arg3 = args[2]; //INPUT_FILE3 -- drugbankDrugs_normalized
		String arg4 = args[3]; //INPUT_FILE4 -- pharmgkbDrugs_normalized
		String arg5 = args[4]; //INPUT_FILE5 -- umlsChemicalAndDrugsSynonyms
		String arg6 = args[5]; //OUTPUT_FILE1 -- drugsDictionaryLoop1
		String arg7 = args[6]; //OUTPUT_FILE2 -- synonymsCountLoop1
		String arg8 = args[7]; //start number for customized ID
		
		//get unique id
		long number = Long.parseLong(arg8); //00000000;
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr);
			while((line = br0.readLine()) != null) {
				drugBankSynonyms.add(line);
			}
			
			FileInputStream fis1 = new FileInputStream(arg2);
			InputStreamReader isr1 = new InputStreamReader(fis1,"UTF-8");
		    BufferedReader br1 = new BufferedReader(isr1);
			while((line = br1.readLine()) != null) {
				pharmGKBSynonyms.add(line);
			}
			
			FileInputStream fis2 = new FileInputStream(arg3);
			InputStreamReader isr2 = new InputStreamReader(fis2,"UTF-8");
		    BufferedReader br00 = new BufferedReader(isr2);
			while((line = br00.readLine()) != null) {
				normalizedDrugBankSynonyms.add(line);
			}
			
			FileInputStream fis3 = new FileInputStream(arg4);
			InputStreamReader isr3 = new InputStreamReader(fis3,"UTF-8");
		    BufferedReader br11 = new BufferedReader(isr3);
			while((line = br11.readLine()) != null) {
				normalizedPharmGKBSynonyms.add(line);
			}
			
			//drug names of length <=4
			FileInputStream fis4 = new FileInputStream("resources/lookup/drugs.txt");
			InputStreamReader isr4 = new InputStreamReader(fis4,"UTF-8");
		    BufferedReader br2 = new BufferedReader(isr4);
			while((line = br2.readLine()) != null) {
				shortNameDrugs.add(line.toLowerCase().trim());
			}
				
			FileInputStream fis5 = new FileInputStream(arg5);
			InputStreamReader isr5 = new InputStreamReader(fis5,"UTF-8");
		    BufferedReader br = new BufferedReader(isr5);
		    FileOutputStream fos = new FileOutputStream(arg6);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		    FileOutputStream fos1 = new FileOutputStream(arg7);
		    OutputStreamWriter osr1 = new OutputStreamWriter(fos1, "UTF-8");
		    BufferedWriter bw1 = new BufferedWriter(osr1);
		    
			while((line = br.readLine()) != null) {
				//generate unique drugID
				number = number+1;
				drugID = "CD".concat(String.format("%08d", number));
				
				//get all UMLS synonyms
				ArrayList<String> synonyms = new ArrayList<String>();
				String[] arrLine = line.split("###");
				cuiTui = arrLine[0];
				for(int i=1; i<arrLine.length; i++) synonyms.add(arrLine[i]);
				
				//get synonyms from DrugBank that are not in UMLS Metathesaurus
				DrugBankPair drugBankPair = drugBankSynonymsCollector.getSynonyms(synonyms, drugBankSynonyms, normalizedDrugBankSynonyms, shortNameDrugs);
				ArrayList<String> drugBankID = drugBankPair.getDrugBankID();
				ArrayList<String> synonyms1 = drugBankPair.getDrugBankSynonyms();
	
				//get synonyms from PharmGKB that are not in UMLS Metathesaurus and DrugBank
				PharmGKBPair pharmGKBPair = pharmGKBSynonymsCollector.getSynonyms(synonyms, synonyms1, pharmGKBSynonyms, normalizedPharmGKBSynonyms, shortNameDrugs);
				ArrayList<String> pharmGKBID = pharmGKBPair.getPharmGKBID();
				ArrayList<String> synonyms2 = pharmGKBPair.getPharmGKBSynonyms();
				
				//combine UMLS synonyms
				String umlsSynonyms = "";
				for(String eachSynonym : synonyms) {
					if(umlsSynonyms.isEmpty()) umlsSynonyms = eachSynonym;
					else umlsSynonyms = umlsSynonyms+"###"+eachSynonym;
				}
				
				//combine DrugBank synonyms
				String drugBankSynonyms = "";
				if(synonyms1.size() > 0) {
					for(String eachSynonym : synonyms1) {
						if(drugBankSynonyms.isEmpty()) drugBankSynonyms = eachSynonym;
						else drugBankSynonyms = drugBankSynonyms+"###"+eachSynonym;
					}
				}
				
				//combine PharmGKB synonyms
				String pharmGKBSynonyms = "";
				if(synonyms2.size() > 0) {
					for(String eachSynonym : synonyms2) {
						if(pharmGKBSynonyms.isEmpty()) pharmGKBSynonyms = eachSynonym;
						else pharmGKBSynonyms = pharmGKBSynonyms+"###"+eachSynonym;
					}
				}
				
				//combine DrugBankID
				String drugBankIDList="";
				if(drugBankID.size() > 0) {
					for(String eachID : drugBankID) {
						if(drugBankIDList.isEmpty()) drugBankIDList = eachID;
						else drugBankIDList = drugBankIDList+"###"+eachID;
					}
				}
				
				//combine PharmGKBID
				String pharmGKBIDList="";
				if(pharmGKBID.size() > 0) {
					for(String eachID : pharmGKBID) {
						if(pharmGKBIDList.isEmpty()) pharmGKBIDList = eachID;
						else pharmGKBIDList = pharmGKBIDList+"###"+eachID;
					}
				}
				
				//write synonyms to file
				if(drugBankIDList.isEmpty()) drugBankSynonyms=""; synonyms1.clear();
				if(pharmGKBIDList.isEmpty()) pharmGKBSynonyms=""; synonyms2.clear();
				
				bw.append(drugID+"\t"+umlsSynonyms+"\t"+drugBankSynonyms+"\t"+pharmGKBSynonyms+"\t"+cuiTui+"\t"+drugBankIDList+"\t"+pharmGKBIDList);
				bw.append("\n");
				
				bw1.append(drugID+"\t"+synonyms.size()+"\t"+synonyms1.size()+"\t"+synonyms2.size()+"\t"+cuiTui+"\t"+drugBankIDList+"\t"+pharmGKBIDList);
				bw1.append("\n");
				
				cuiTui="";
				
				count++;
				if(count%50000==0) {
					long stopTime = System.currentTimeMillis();
				    long elapsedTime = stopTime - startTime;
				    System.out.println(count+" concepts are processed in " + elapsedTime);
				}
			}
			
			br0.close();
			br1.close();
			br00.close();
			br11.close();
			br2.close();
			br.close();
			bw.close();
			bw1.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
}
