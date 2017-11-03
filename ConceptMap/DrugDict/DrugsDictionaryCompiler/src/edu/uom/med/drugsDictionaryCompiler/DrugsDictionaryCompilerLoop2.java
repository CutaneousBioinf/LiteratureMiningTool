package edu.uom.med.drugsDictionaryCompiler;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import edu.uom.med.drugsDictionaryCompiler.PharmGKBSynonymsCollector.PharmGKBPair;

/**
 * Program is meabt to test the mapping of synonyms between DrugBank and PharmGKB. This is the second step of the chemicals/drugs
 * lexicon generation. DrugBank is considered as the starting resource. For each drug from DrugBank (that are not in UMLS 
 * Metathesaurus), we collected all the synonyms. Then we explored synonyms for the same drug in PharmGKB and combined the ones 
 * that are not present in DrugBank.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugsDictionaryCompilerLoop2 {
	
	//global attributes
	static ArrayList<String> pharmGKBSynonyms = new ArrayList<String>();
	static ArrayList<String> normalizedPharmGKBSynonyms = new ArrayList<String>();
	static PharmGKBSynonymsCollector pharmGKBSynonymsCollector = new PharmGKBSynonymsCollector();
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="", cuiTui="", drugBankID="", drugID="";
		int count=0;
		
		ArrayList<String> shortNameDrugs = new ArrayList<String>();
		
		String arg1 = args[0]; //INPUT_FILE1 -- pharmgkbSynonyms 
		String arg2 = args[1]; //INPUT_FILE2 -- pharmgkbDrugs_normalized 
		String arg3 = args[2]; //INPUT_FILE3 -- drugBankSynonyms
		String arg4 = args[3]; //OUTPUT_FILE1 -- drugsDictionaryLoop2
		String arg5 = args[4]; //OUTPUT_FILE2 -- synonymsCountLoop2
		
		//get unique id
		long number = 576235; //ID number from last record of DrugsDictionaryCompilerLoop1.java + 1
		
		try {
			FileInputStream fis1 = new FileInputStream(arg1);
			InputStreamReader isr1 = new InputStreamReader(fis1,"UTF-8");
		    BufferedReader br1 = new BufferedReader(isr1);
			while((line = br1.readLine()) != null) {
				pharmGKBSynonyms.add(line);
			}
			
			FileInputStream fis3 = new FileInputStream(arg2);
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
			
			FileInputStream fis5 = new FileInputStream(arg3);
			InputStreamReader isr5 = new InputStreamReader(fis5,"UTF-8");
		    BufferedReader br = new BufferedReader(isr5);
		    FileOutputStream fos = new FileOutputStream(arg4);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		    FileOutputStream fos1 = new FileOutputStream(arg5);
		    OutputStreamWriter osr1 = new OutputStreamWriter(fos1, "UTF-8");
		    BufferedWriter bw1 = new BufferedWriter(osr1);
		    
		    while((line = br.readLine()) != null) {
				//generate unique drugID
				number = number+1;
				drugID = "CD".concat(String.format("%08d", number));
				
				//get all DrugBank synonyms
				ArrayList<String> synonyms = new ArrayList<String>(); //UMLS synonyms is empty in Loop2
				ArrayList<String> synonyms1 = new ArrayList<String>();
				String[] arrLine = line.split("###");
				drugBankID = arrLine[0];
				for(int i=1; i<arrLine.length; i++) synonyms1.add(arrLine[i]);
				
				//get synonyms from PharmGKB that are not in DrugBank
				PharmGKBPair pharmGKBPair = pharmGKBSynonymsCollector.getSynonyms(synonyms, synonyms1, pharmGKBSynonyms, normalizedPharmGKBSynonyms, shortNameDrugs);
				ArrayList<String> pharmGKBID = pharmGKBPair.getPharmGKBID();
				ArrayList<String> synonyms2 = pharmGKBPair.getPharmGKBSynonyms();
				
				//combine UMLS synonyms
				String umlsSynonyms = "";
				
				//combine DrugBank synonyms
				String drugBankSynonyms = "";
				for(String eachSynonym : synonyms1) {
					if(drugBankSynonyms.isEmpty()) drugBankSynonyms = eachSynonym;
					else drugBankSynonyms = drugBankSynonyms+"###"+eachSynonym;
				}
				
				//combine PharmGKB synonyms
				String pharmGKBSynonyms = "";
				if(synonyms2.size() > 0) {
					for(String eachSynonym : synonyms2) {
						if(pharmGKBSynonyms.isEmpty()) pharmGKBSynonyms = eachSynonym;
						else pharmGKBSynonyms = pharmGKBSynonyms+"###"+eachSynonym;
					}
				}
				
				//combine PharmGKB ID
				String pharmGKBIDList="";
				if(pharmGKBID.size() > 0) {
					for(String eachID : pharmGKBID) {
						if(pharmGKBIDList.isEmpty()) pharmGKBIDList = eachID;
						else pharmGKBIDList = pharmGKBIDList+"###"+eachID;
					}
				}
				
				//write synonyms to file
				if(drugBankID.isEmpty()) drugBankSynonyms=""; synonyms1.clear();
				if(pharmGKBIDList.isEmpty()) pharmGKBSynonyms=""; synonyms2.clear();
				
				bw.append(drugID+"\t"+umlsSynonyms+"\t"+drugBankSynonyms+"\t"+pharmGKBSynonyms+"\t"+cuiTui+"\t"+drugBankID+"\t"+pharmGKBIDList);
				bw.append("\n");
				
				bw1.append(drugID+"\t"+synonyms.size()+"\t"+synonyms1.size()+"\t"+synonyms2.size()+"\t"+cuiTui+"\t"+drugBankID+"\t"+pharmGKBIDList);
				bw1.append("\n");
				
				drugBankID="";
				
				count++;
				if(count%1000==0) {
					long stopTime = System.currentTimeMillis();
				    long elapsedTime = stopTime - startTime;
				    System.out.println(count+" concepts are processed in "+elapsedTime);
				}
			}
			
			br1.close();
			br2.close();
			br11.close();
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
