package edu.uom.med.drugBankFileProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Project is meant to extract the various groups defined for drugs in DrugBank. A drug is assigned with one or more groups and
 * the groups briefly describes the category of drug (e.g. experimental drug, withdrawn in countries such as ...).
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugBankGroupExtractor {
	
	/**
	 * Program executions starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="", id="", drugbankID="", group="";

		String arg1 = args[0]; //INPUT_FILE -- drugbank.xml
		String arg2 = args[1]; //OUTPUT_FILE -- drugbankID_group
		
		ArrayList<String> groups = new ArrayList<String>();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		     
			while((line=br.readLine()) != null) {
				if(line.trim().startsWith("<drugbank-id ")) {
					id = "";
					id = line.substring(line.indexOf("\">")+2, line.indexOf("</"));
					if(!id.contains("DBSALT")) {
						drugbankID = id;
					}
				}
				else if(line.trim().startsWith("<group>")) {
					group = line.substring(line.indexOf(">")+1, line.indexOf("</"));
					groups.add(group);
				}
				else if(line.trim().equals("</drug>")) {
					String groupList="";
					for(String eachGroup : groups) {
						if(groupList=="") groupList = eachGroup;
						else groupList = groupList+"###"+eachGroup;
					}
					bw.append(drugbankID+"\t"+groupList);
					bw.append("\n");
					
					id="";
					groups.clear();
				}
			}
			
			br.close();
			bw.close();
		} catch(IOException e) {
			System.out.println(e);
		}

		long stopTime = System.currentTimeMillis();
    	long elapsedTime = stopTime - startTime;
    	System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
}
