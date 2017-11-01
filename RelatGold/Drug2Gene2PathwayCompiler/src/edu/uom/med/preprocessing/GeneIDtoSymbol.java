package edu.uom.med.preprocessing;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Program maps gene ID to gene symbol
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class GeneIDtoSymbol {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //geneID_NameSymbolAliases_from_Entrez
		String arg2 = args[1]; //OUTPUT_FILE_REACTOME
		String arg3 = args[2]; //OUTPUT_FILE_REACTOME_GeneSymbol

		String line="";
		
		HashMap<String, String> geneIDandSymbol = new LinkedHashMap<String, String>();
		
		try {
			FileReader fr0 = new FileReader(arg1);
			BufferedReader br0 = new BufferedReader(fr0);
			while((line=br0.readLine()) != null) {
				String[] arrLine = line.split("\t");
				String[] geneAnnotation = arrLine[1].split("$$$");
				geneIDandSymbol.put(arrLine[0], geneAnnotation[0]);
			}
			
			FileReader fr = new FileReader(arg2);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg3);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line=br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				String geneID = arrLine[0].substring(arrLine[0].lastIndexOf("-")+1).trim();
				String geneSymbol = geneIDandSymbol.get(geneID);
				
				bw.append(geneSymbol + "\t" + arrLine[1]);
				bw.append("\t");
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
