package edu.uom.med.drugBankFileProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * File: drugbank.xml is downloaded from DrugBank database. This program processes the downloaded file to extract drugs, their
 * synonyms and salts. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugNameSynonymExtractor {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String arg1 = args[0]; //INPUT_FILE -- drugbank.xml
		String arg2 = args[1]; //OUTPUT_FILE1_drugbankID_drugs_synonyms
		String arg3 = args[2]; //OUTPUT_FILE2_salts_info
		String arg4 = args[3]; //OUTPUT_FILE3_drugbankID_drugsalts

		String line="", id="", drugbankID="", drugbankSaltID="", record="", name="", saltName="";
		int count=0, count1=0;
		boolean flag=false, flag1=false;
		
		ArrayList<String> drugName = new ArrayList<String>();
		ArrayList<String> drugNameList = new ArrayList<String>();
		ArrayList<String> synonyms = new ArrayList<String>();
		
		ArrayList<String> saltTags = new ArrayList<String>();
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			FileWriter fw1 = new FileWriter(arg3);
			BufferedWriter bw1 = new BufferedWriter(fw1);
			
			while((line=br.readLine()) != null) {
				if(line.trim().startsWith("<drugbank-id ")) {
					record = "";
					id = "";
					//drugName.clear();
					synonyms.clear();
					saltTags.clear();
					
					id = line.substring(line.indexOf("\">")+2, line.indexOf("</"));
					if(!id.contains("DBSALT")) {
						drugbankID = id;
					}
				}
				else if(line.trim().startsWith("<name>")) {
					if(!flag1) {
						name = line.substring(line.indexOf(">")+1, line.indexOf("</"));
						if(drugName.isEmpty()) {
							drugName.add(name);
							if(!drugNameList.contains(name)) {
								drugNameList.add(name);
							}
						}
						flag1=true;
					}
				}
				else if(line.trim().startsWith("<synonym ")) {
					flag=false;
					String synonym = line.substring(line.indexOf(">")+1, line.indexOf("</"));
					synonyms.add(synonym.trim());
				}
				else if(line.trim().equals("</drug>")) {
					record = writeToFile(drugbankID, drugName, synonyms);
					bw.append(record);
					bw.append("\n");
											
					name="";
					flag1 = false;
					drugName.clear();
					count++;
					//if(count==5000) break;
				}
				
				if(line.trim().startsWith("<salts>")) {
					flag = true;
					saltTags.add(line.trim());
				}
				else if(line.trim().startsWith("</salts>")) {
					flag = false;
					saltTags.add(line.trim());
				}	
				
				if(flag) { //for processing salt information
					saltTags.add(line.trim());
					bw1.append(line.trim());
					bw1.append("\n");
				}
			}
			
			br.close();
			bw.close();
			bw1.close();
		} catch(IOException e) {
			System.out.println(e);
		}
		
		//write salts to file
		ArrayList<String> saltsList = processSalts(arg3);
		try {
			FileWriter fw = new FileWriter(arg4);
			BufferedWriter bw = new BufferedWriter(fw);
			for(String eachSalt : saltsList) {
				bw.append(eachSalt);
				bw.append("\n");
			}
			bw.close();
		}catch(IOException e) {
			System.out.println(e);
		}
		
		System.out.println(count);
		System.out.println(count1);

		long stopTime = System.currentTimeMillis();
    	long elapsedTime = stopTime - startTime;
    	System.out.println("Execution time in milliseconds: " + elapsedTime);
		
	}
	
	/**
	 * Program to group drug names and synonyms assigned to each drug ID and returns the same as one drug record. 
	 * 
	 * @param drugbankID
	 * @param drugName
	 * @param synonyms
	 * @return
	 */
	
	public static String writeToFile(String drugbankID, ArrayList<String> drugName, 
			ArrayList<String> synonyms) {
		String record="";
		
		if(!drugbankID.isEmpty()) {
			String drugsList="", synonymsList="";
			
			if(!drugName.isEmpty()) {
				for(String eachDrugName : drugName) {
					if(drugsList.isEmpty()) { drugsList = eachDrugName; }
					else { drugsList = drugsList+"###"+eachDrugName; }
				}
			}
			if(!synonyms.isEmpty()) {
				for(String eachSynonym : synonyms) {
					if(synonymsList.isEmpty()) { synonymsList = eachSynonym; }
					else { synonymsList = synonymsList+"###"+eachSynonym; }
				}
			}
			
			record = drugbankID+"\t"+drugsList+"\t"+synonymsList;
		}
		
		return record;
	}
	
	/**
	 * Program processes drug salts in DrugBank xml file and retrieves their ID, name. We observed that the ID is different for
	 * drugs and their corresponding salts in DrugBank. Therefore, we also assigned different customized ID for drugs and their
	 * corresponding salts.
	 * 
	 * @return
	 */
	
	public static ArrayList<String> processSalts(String arg3) {
		String line="", drugbankSaltID="", saltName="";
		ArrayList<String> saltsList = new ArrayList<String>();
		int count=0;
		
		try { 
			FileReader fr = new FileReader(arg3);
			BufferedReader br = new BufferedReader(fr);
			while((line=br.readLine())!=null) {
				if(line.startsWith("<drugbank-id ")) {
					drugbankSaltID = line.substring(line.indexOf("\">")+2, line.indexOf("</"));
				}
				if(line.startsWith("<name>")) {
					saltName = line.substring(line.indexOf(">")+1, line.indexOf("</"));
				}
				
				if(line.startsWith("</salt>")) {
					saltsList.add(drugbankSaltID+"\t"+saltName);
					count++;
				}
			}
			System.out.println(count);
			br.close();
		}catch(IOException e) {
			System.err.println(e);
		}
		
		return saltsList;
	}
	
}

