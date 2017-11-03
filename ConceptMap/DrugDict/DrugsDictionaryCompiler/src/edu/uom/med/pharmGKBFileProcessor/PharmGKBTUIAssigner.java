package edu.uom.med.pharmGKBFileProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Program to assign a customized semantic type (TUI) for the drugs/synonyms from PharmGKB.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class PharmGKBTUIAssigner {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- PharmGKB_drugs_synonyms
		String arg2 = args[1]; //OUTPUT_FILE -- PharmGKBID_TUI_drugs_synonyms

		String line="", id="";
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				id = line.substring(0, line.indexOf("\t"));
				id = id+"_T777";
				line = line.replace(line.substring(0, line.indexOf("\t")), id);
				bw.append(line);
				bw.append("\t");
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
	
}
