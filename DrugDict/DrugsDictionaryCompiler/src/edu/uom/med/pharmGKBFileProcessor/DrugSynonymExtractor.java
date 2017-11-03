package edu.uom.med.pharmGKBFileProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Program to extract drugs synonyms from PharmGKB
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugSynonymExtractor {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String arg1 = args[0]; //INPUT_FILE -- PharmGKB_drugs_synonyms
		String arg2 = args[1]; //OUTPUT_FILE_pharmgkb 

		String line="";
		int count=0;
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine()) != null) {
				ArrayList<String> genericNames = new ArrayList<String>();
				ArrayList<String> tradeNames = new ArrayList<String>();
				ArrayList<String> drugBankID = new ArrayList<String>();
				
				String[] arrLine = line.split("\t");
				String pharmGKBID = arrLine[0];
				String drugName = arrLine[1];
				
				if(arrLine.length==3) { //generic name
					String gNames = arrLine[2];
					genericNames = getGenericNames(gNames);
				}
				else if(arrLine.length==4) { //generic name and trade name
					if(!arrLine[2].isEmpty()) {
						String gNames = arrLine[2];
						genericNames = getGenericNames(gNames);
					}
					if(!arrLine[3].isEmpty()) {
						String tNames = arrLine[3];
						tradeNames = getTradeNames(tNames);
					}
				}
				else if(arrLine.length==5) { //generic name, trade name and cross reference
					if(!arrLine[2].isEmpty()) {
						String gNames = arrLine[2];
						genericNames = getGenericNames(gNames);
					}
					if(!arrLine[3].isEmpty()) {
						String tNames = arrLine[3];
						tradeNames = getTradeNames(tNames);
					}
					if(!arrLine[4].isEmpty()) {
						String crossRef = arrLine[4];
						drugBankID = getDrugBankID(crossRef);
					}
				}
				
				//write to file
				String drugInfo = compileNames(pharmGKBID, drugName, genericNames, tradeNames, drugBankID);
				bw.append(drugInfo);
				bw.append("\n");
				
				count++;
				//if(count==10) break;
			}
			
			System.out.println(count);
			
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
    	long elapsedTime = stopTime - startTime;
    	System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
	/**
	 * Program to retrieve generic names for a given drug
	 * 
	 * @param gNames
	 * @return
	 */
	
	public static ArrayList<String> getGenericNames(String gNames) {
		ArrayList<String> genericNames = new ArrayList<String>();
		
		String[] genericNamesList = gNames.split(",\"");
		for(String eachName : genericNamesList) {
			if(eachName.matches("(.+?)\"")) {
				String name = eachName.substring(0, eachName.length()-1);
				genericNames.add(name);
			}
			else {
				genericNames.add(eachName);
			}
		}
		
		return genericNames;
	}
	
	/**
	 * Program to retrieve trade names for a drug
	 * 
	 * @param tNames
	 * @return
	 */
	
	public static ArrayList<String> getTradeNames(String tNames) {
		ArrayList<String> tradeNames = new ArrayList<String>();
		
		String[] tradeNamesList = tNames.split(",\"");
		for(String eachName : tradeNamesList) {
			if(eachName.matches("(.+?)\"")) {
				String name = eachName.substring(0, eachName.length()-1);
				tradeNames.add(name);
			}
			else {
				tradeNames.add(eachName);
			}
		}
		
		return tradeNames;
	}
	
	/**
	 * Program to get DrugBank ID. This is used for cross reference, becasue PharmGKB gives additional details about 
	 * drugs appearing in DrugBank or ClinicalTrials databases.
	 * 
	 * @param crossRef
	 * @return
	 */
	
	public static ArrayList<String> getDrugBankID(String crossRef) {
		ArrayList<String> drugBankID = new ArrayList<String>();
		
		String[] crossRefList = crossRef.split(",\"");
		for(String eachRef : crossRefList) {
			if(eachRef.matches("(.+?)\"")) {
				String ref = eachRef.substring(0, eachRef.length()-1);
				if(ref.startsWith("DrugBank:")) {
					String dbID = ref.substring(ref.indexOf(":")+1);
					drugBankID.add(dbID);
				}
				else if(ref.startsWith("ClinicalTrials.gov:")) {
					String dbID = ref.substring(ref.indexOf(":")+1);
					drugBankID.add(dbID);
				}
			}
			else {
				if(eachRef.startsWith("DrugBank:")) {
					String dbID = eachRef.substring(eachRef.indexOf(":")+1);
					drugBankID.add(dbID);
				}
				else if(eachRef.startsWith("ClinicalTrials.gov:")) {
					String dbID = eachRef.substring(eachRef.indexOf(":")+1);
					drugBankID.add(dbID);
				}
			}
		}
		
		return drugBankID;
	}
	
	/**
	 * Program to compile drug name, generic names, trade names along with DrugBank ID details for every drug in 
	 * PharmGKB.
	 * 
	 * @param pharmGKBID
	 * @param drugName
	 * @param genericNames
	 * @param tradeNames
	 * @param drugBankID
	 * @return
	 */
	
	public static String compileNames(String pharmGKBID, String drugName, ArrayList<String> genericNames, 
			ArrayList<String> tradeNames, ArrayList<String> drugBankID) {
		String drugInfo="", genericNameList="", tradeNameList="", drugBankIDList="", separator="###";
		
		if(!genericNames.isEmpty()) {
			for(String eachGName : genericNames) {
				if(genericNameList.isEmpty()) {
					genericNameList = eachGName;
				}
				else {
					genericNameList = genericNameList.concat(separator).concat(eachGName);
				}
			}
		}
		
		if(!tradeNames.isEmpty()) {
			for(String eachTName : tradeNames) {
				if(tradeNameList.isEmpty()) {
					tradeNameList = eachTName;
				}
				else {
					tradeNameList = tradeNameList.concat(separator).concat(eachTName);
				}
			}
		}
		
		if(!drugBankID.isEmpty()) {
			for(String eachID : drugBankID) {
				if(drugBankIDList.isEmpty()) {
					drugBankIDList = eachID;
				}
				else {
					drugBankIDList = drugBankIDList.concat(separator).concat(eachID);
				}
			}
		}
		
		drugInfo = pharmGKBID+"\t"+drugName+"\t"+genericNameList+"\t"+tradeNameList+"\t"+drugBankIDList;
		
		return drugInfo;
	}
	
}
