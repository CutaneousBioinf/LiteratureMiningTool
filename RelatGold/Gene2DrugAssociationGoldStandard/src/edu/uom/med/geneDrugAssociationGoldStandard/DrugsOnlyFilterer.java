package edu.uom.med.geneDrugAssociationGoldStandard;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Project to filter disease-drugs only associations from disease-chemical/drug associations.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugsOnlyFilterer {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INTERACTIONS_OUTPUT_FILE_uniquelist
		String arg2 = args[1]; //INTERACTIONS_OUTPUT_FILE_uniquelist_drugsonly

		String line="";
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(!arrLine[2].contains("_T195") &&  				//Antibiotics
						!arrLine[2].contains("_T200") &&			//Clinical drugs
						!arrLine[2].contains("_T121")) continue;	//Pharmacologically active substances
				
				bw.append(line);
				bw.append("\n");
			}
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
      	long elapsedTime = stopTime - startTime;
      	System.out.println("Execution time: "+elapsedTime);
	}
	
}
