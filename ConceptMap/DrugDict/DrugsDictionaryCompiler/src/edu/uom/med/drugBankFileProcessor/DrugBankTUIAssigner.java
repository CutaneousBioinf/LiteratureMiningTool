package edu.uom.med.drugBankFileProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;

/**
 * We assigned a customized semantic type (TUI) for drugs and synonyms in DrugBank, such that these TUI are utilized while
 * creating customized ID for the chemicals/drugs in Chemicals/Drugs lexicon. The program assigns a customized TUI for drugs and
 * drug salts in DrugBank.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugBankTUIAssigner {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- drugbankID_TUI
		String arg2 = args[1]; //INPUT_FILE -- drugbankID_drugsAndSalts_synonyms
		String arg3 = args[2]; //OUTPUT_FILE -- drugbankID_TUI_drugsAndSalts_synonyms

		String line="", id="";
		
		LinkedHashMap<String, String> customizedTUI = new LinkedHashMap<String, String>();
		
		try {
			FileInputStream fis0 = new FileInputStream(arg1);
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
		    	String drugbankID = line.substring(0, line.indexOf("_"));
		    	customizedTUI.put(drugbankID, line.trim());
		    }
		
			FileInputStream fis = new FileInputStream(arg2);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(arrLine[0].startsWith("DBSALT")) {
					id = arrLine[0]+"_T000";
					if(arrLine.length == 2) {
						bw.append(id+"\t"+arrLine[1]);
						bw.append("\n");
					}
					else if(arrLine.length == 3) {
						bw.append(id+"\t"+arrLine[1]+"\t"+arrLine[2]);
						bw.append("\n");
					}
				}
				else {
					id = customizedTUI.get(arrLine[0].trim());
					if(arrLine.length == 2) {
						bw.append(id+"\t"+arrLine[1]);
						bw.append("\n");
					}
					else if(arrLine.length == 3) {
						bw.append(id+"\t"+arrLine[1]+"\t"+arrLine[2]);
						bw.append("\n");
					}
				}
			}
			
			br0.close();
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
    	long elapsedTime = stopTime - startTime;
    	System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
}
