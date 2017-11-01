package edu.uom.med.geneDrugAssociationGoldStandard;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Program generates a unique list of gene-drug association from CTD, DrugBank and PharmGKB
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class GeneDrugAssociationGSUniqueList {
	
	/**
	 * Program execution starts from here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="";

		String arg1 = args[0]; //INTERACTIONS_OUTPUT_FILE
		String arg2 = args[1]; //INTERACTIONS_OUTPUT_FILE_uniquelist
		
		ArrayList<String> associations = new ArrayList<String>();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(!associations.contains(arrLine[0] + "\t" + arrLine[2])) {
					bw.append(line);
					bw.append("\n");
					
					associations.add(arrLine[0] + "\t" + arrLine[2]);
				}
			}
			
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
    	long elapsedTime = stopTime - startTime;
    	System.out.println("Execution in milliseconds: " + elapsedTime);
	}
	
}
