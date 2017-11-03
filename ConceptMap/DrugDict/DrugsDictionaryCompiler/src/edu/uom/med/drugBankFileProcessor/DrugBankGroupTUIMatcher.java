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
 * Program assigns cutomized semantic type identifier (TUI) for the drugs from DrugBank. This is helpful when combining the 
 * drugs and synonyms from DrugBank with UMLS Metathesaurus.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugBankGroupTUIMatcher {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String arg1 = args[0]; //drugbankID_group
		String arg2 = args[1]; //drugbankID_TUI

		String line="", tui="";
		
		LinkedHashMap<String, String> customizedTUI = getTUI();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				tui = customizedTUI.get(arrLine[1].trim());
				bw.append(arrLine[0]+"_"+tui);
				bw.append("\n");
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
	 * Method to define unique TUI for different combinations of drug grouped from DrugBank
	 * 
	 * @return
	 */

	public static LinkedHashMap<String, String> getTUI() {
		LinkedHashMap<String, String> customizedTUI = new LinkedHashMap<String, String>();
		
		customizedTUI.put("approved", "T220");
		customizedTUI.put("approved###experimental", "T221");
		customizedTUI.put("approved###illicit", "T222");
		customizedTUI.put("approved###illicit###investigational", "T223");
		customizedTUI.put("approved###illicit###investigational###withdrawn", "T224");
		customizedTUI.put("approved###illicit###withdrawn", "T225");
		customizedTUI.put("approved###investigational", "T226");
		customizedTUI.put("approved###investigational###nutraceutical", "T227");
		customizedTUI.put("approved###investigational###withdrawn", "T228");
		customizedTUI.put("approved###nutraceutical", "T229");
		customizedTUI.put("approved###nutraceutical###withdrawn", "T230");
		customizedTUI.put("approved###withdrawn", "T231");
		customizedTUI.put("experimental", "T330");
		customizedTUI.put("experimental###illicit", "T331");
		customizedTUI.put("experimental###illicit###withdrawn", "T332");
		customizedTUI.put("experimental###investigational", "T333");
		customizedTUI.put("illicit###investigational", "T440");
		customizedTUI.put("illicit###investigational###withdrawn", "T441");
		customizedTUI.put("investigational", "T442");
		customizedTUI.put("investigational###nutraceutical", "T443");
		customizedTUI.put("investigational###withdrawn", "T444");
		customizedTUI.put("illicit", "T550");
		customizedTUI.put("illicit###withdrawn", "T551");
		customizedTUI.put("nutraceutical", "T660");
		customizedTUI.put("withdrawn", "T000");
		
		return customizedTUI;
	}
	
}
