package edu.uom.med.ctdProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Comparative Toxicogenomics Database (CTD) contains curated and inferred disease-gene
 * associations. Program filters the curated associations by considering the information
 * in the 5th column (i.e. Direct evidence - marker/mechanism OR therapeutic OR 
 * marker/mechanism|therapeutic).
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class CuratedAssociationRetriever {

	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="";
		int count=0;
		
		String arg1 = args[0]; //INPUT FILE -- OUTPUT_FILE_noIntroSection (refer Documentation/Gene_disease_association.docx)
		String arg2 = args[1]; //OUTPUT FILE -- OUTPUT_FILE_curatedAssociations
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				if(arrLine[4].equals("marker/mechanism") ||
						arrLine[4].equals("therapeutic") ||
						arrLine[4].equals("marker/mechanism|therapeutic")) {
					bw.append(line);
					bw.append("\n");
				}
				
				count++;
				if(count==10) break;
				//if(count%1000=0) System.out.println(count);
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
