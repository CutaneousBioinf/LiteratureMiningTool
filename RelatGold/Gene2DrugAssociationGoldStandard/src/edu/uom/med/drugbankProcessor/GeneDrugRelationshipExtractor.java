package edu.uom.med.drugbankProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Program processes the downloaded file from DrugBank and extracts gene-drug relationships. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class GeneDrugRelationshipExtractor {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //drugbank.xml
		String arg2 = args[1]; //OUTPUT_FILE

		String line="", id="", drugbankID="", name="", geneSymbol="";
		int count=0;
		boolean flag=false, flag1=false, flag2=false;
		
		ArrayList<String> drugName = new ArrayList<String>();
		ArrayList<String> drugNameList = new ArrayList<String>();
		ArrayList<String> synonyms = new ArrayList<String>();
		ArrayList<String> genes = new ArrayList<String>();
		
		ArrayList<String> saltTags = new ArrayList<String>();
		
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				if(line.trim().startsWith("<drugbank-id ")) {
					id = "";
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
				else if(line.trim().startsWith("<target ")) {
					flag2=true;
				}
				else if(line.trim().equals("</target>")) {
					flag2=false;
				}
				else if(line.trim().equals("</drug>")) {
					ArrayList<String> records = writeToFile(drugbankID, drugName, synonyms, genes);
					
					for(String eachRecord : records) {
						bw.append(eachRecord);
						bw.append("\n");
					}
											
					name="";
					flag1 = false;
					drugName.clear();
					genes.clear();
					count++;
					//if(count==5000) break;
				}
				
				if(flag2 && line.trim().startsWith("<gene-name>")) {
					geneSymbol = line.substring(line.indexOf(">")+1, line.indexOf("</"));
					genes.add(geneSymbol);
				}
			}
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
	 * Program groups the drug names and synonyms and make it ready to write to a file
	 * 
	 * @param drugbankID
	 * @param drugName
	 * @param synonyms
	 * @param genes
	 * @return
	 */
	
	public static ArrayList<String> writeToFile(String drugbankID, ArrayList<String> drugName, 
			ArrayList<String> synonyms, ArrayList<String> genes) {
		String record="";
		
		ArrayList<String> records = new ArrayList<String>();
		
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
			
			for(String eachGene : genes) {
				record = eachGene+"\t"+drugsList+"\t"+synonymsList+"\t"+drugbankID;
				records.add(record);
			}
		}
		
		return records;
	}
	
}
