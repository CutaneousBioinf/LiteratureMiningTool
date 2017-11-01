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
 * Program collects a unique list of disease-drug association from Comparative Toxicogenomics Database (CTD) and NDF-RT
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DiseaseChemicalORDrugAssociationUniqueList {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
 
		String arg1 = args[0]; //INPUT_FILE -- OUTPUT_FILE_CTD_NDFRT
		String arg2 = args[1]; //OUTPUT_FILE --	OUTPUT_FILE_CTD_NDFRT_unique	

		String line="";
		int count=0;
		
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
				
				if(!associations.contains(arrLine[2] + "\t" + arrLine[3])) {
					bw.append(line);
					bw.append("\n");
					
					associations.add(arrLine[2] + "\t" + arrLine[3]);
				}

				count++;
				//if(count==25) break;	
				if(count%100 == 0) System.out.println(count);
			}
			
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Execution time in milliseconds: "+elapsedTime);
	}
	
}
