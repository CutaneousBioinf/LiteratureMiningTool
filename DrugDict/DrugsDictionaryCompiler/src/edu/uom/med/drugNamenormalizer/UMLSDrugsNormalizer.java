package edu.uom.med.drugNameNormalizer;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Program normalizes the chemical/drug name.  
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class UMLSDrugsNormalizer {

	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- umls_chemicals_and_drugs_synonyms
		String arg2 = args[1]; //OUTPUT_FILE -- umlsDrugsSynonyms_normalized
		
		String line="";
		int count=0;
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				count++;
				if(count%100000==0) System.out.println(count);
				
				if(!line.contains("-")) {
					bw.append(line);
					bw.append("\n");
					continue;
				}
				
				String[] arrLine = line.split("\t");
				if(arrLine[1].contains("-")) {
					arrLine[1] = arrLine[1].replaceAll("-", " ");
					arrLine[1] = arrLine[1].replaceAll(" ", "");
					
					bw.append(arrLine[0]+"\t"+arrLine[1]);
					bw.append("\n");
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

}
