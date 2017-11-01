package edu.uom.med.allResources;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Program filters associations involving chemicals only. We observed that certain diseases are associated only with 
 * chemicals (from disease-drug association from CTD and NDFRT) and developed this program to know what are those diseases.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DiseasesWithChemicalsOnly {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE1 -- DISESE_DRUGSONLY_ASSOCIATION_FILE
		String arg2 = args[1]; //INPUT_FILE2 -- DISESE_CHEMICALS/DRUGS_ASSOCIATION_FILE
		String arg3 = args[2]; //INPUT_FILE3 -- DISESE_CHEMICALSONLY_ASSOCIATION_FILE
		
		String line="";
		
		ArrayList<String> diseasesAssociatedWithDrugsOnly = new ArrayList<String>();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		    while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				arrLine[3] = arrLine[3].trim();
				
				if(!diseasesAssociatedWithDrugsOnly.contains(arrLine[3])) {
					diseasesAssociatedWithDrugsOnly.add(arrLine[3].trim());
				}
			}
		    
		    FileInputStream fis1 = new FileInputStream(arg2);
			InputStreamReader isr1 = new InputStreamReader(fis1,"UTF-8");
		    BufferedReader br1 = new BufferedReader(isr1);
		         
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br1.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				arrLine[3] = arrLine[3].trim();
				
				if(!diseasesAssociatedWithDrugsOnly.contains(arrLine[3])) {
					bw.append(line);
					bw.append("\n");
				}
			}
			br.close();
			br1.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
}
