package edu.uom.med.drugsDictionaryGenerator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Program to remove abbreviations from DrugBank and PharmGKB. We observed that abbreviations create ambiguity between
 * the chemicals/drugs and removed them while processing UMLS Metathesaurus. We applied the same criteria for DrugBank
 * and PharmGKB and removed the abbreviations from them.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugBankAndPharmGKBAbbreviationsRemover {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- AbbreviationsFromDrugBankAndPharmGKB
		String arg2 = args[1]; //INPUT_FILE -- ChemicalsAndDrugsDictionary
		String arg3 = args[2]; //OUTPUT_FILE -- ChemicalsAndDrugsDictionary_abbreviationsRemoved


		String line="";
		
		ArrayList<String> abbreviationRecords = new ArrayList<String>();
		
		try {
			FileInputStream fis0 = new FileInputStream(arg1);
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
				abbreviationRecords.add(line);
			}
			
			FileInputStream fis = new FileInputStream(arg2);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				if(!abbreviationRecords.contains(line)) {
					bw.append(line);
					bw.append("\n");
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
