package edu.uom.med.idMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;

/**
 * Program is to map chemical/drug names to cutomized ID in chemicals/drugs lexicon.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugnameToLexicondrugidMapper {
	
	/**
	 * Program execution starts here.
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- INPUT_FILE_selectedColumns
		String arg2 = args[1]; //OUTPUT_FILE1

		String line="", drugID="";
		int count = 0;
		
		Hashtable<String, String> drugsLexicon = new Hashtable<String, String>();
		
		try {
			//chemicals/drugs lexicon
			FileInputStream fis0 = new FileInputStream("resources/lookup/ChemicalsAndDrugsLexicon");
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
				String[] arrLine = line.split("\\|");
				drugsLexicon.put(arrLine[1], arrLine[4]);

				count++;
				if(count%1000==0) System.out.println(count);
			}
		    System.out.println("Lexicon read is complete!!!");
			
			//input file
		    FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		    
		    //output file    
			FileOutputStream fos = new FileOutputStream(arg2);
			OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				if(line.startsWith("Gene_symbol") || line.startsWith("Drug_name") || line.startsWith("Disease_CUI")) {
					bw.append(line+"\tDrug_ID_from_lexicon");
					bw.append("\n");
					continue;
				}
				
				String[] arrLine = line.split("\t");
				//drugID = drugsLexicon.get(arrLine[0]); // for CTD file
				drugID = drugsLexicon.get(arrLine[3]); // for NDFRT file			

				bw.append(line + "\t" + drugID);
				bw.append("\n");
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
