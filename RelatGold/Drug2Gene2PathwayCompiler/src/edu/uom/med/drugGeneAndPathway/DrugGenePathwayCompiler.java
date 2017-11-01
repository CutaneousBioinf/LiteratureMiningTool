package edu.uom.med.drugGeneAndPathway;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Program to get drug-gene-pathway information for the list of drugs shared by Yilin. Please see the documentation for 
 * details about selected drugs. The program can be modified to retrieve gene-pathway details for any drug(s)
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugGenePathwayCompiler {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //OUTPUT_FILE_drugsonly_gene_association (refer Documentation/Drug_gene_pathway.docx)
		String arg2 = args[1]; //OUTPUT_FILE_goLabel (refer Documentation/Drug_gene_pathway.docx)
		String arg3 = args[2]; //DRUG_GENE_PATHWAY

		String line="", drug="";
		
		HashMap<String, String> drugsAndGenes = new LinkedHashMap<String, String>();
		ArrayList<String> genes = new ArrayList<String>();
		
		try {
			FileReader fr0 = new FileReader(arg1);
			BufferedReader br0 = new BufferedReader(fr0);
			while((line=br0.readLine()) != null) {
				String[] arrLine = line.split("\t");
				drugsAndGenes.put(arrLine[1], arrLine[0]);
				genes.add(arrLine[1]);
			}
			
			FileReader fr = new FileReader(arg2);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg3);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line=br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				if(genes.contains(arrLine[0])) {
					drug = drugsAndGenes.get(arrLine[0]);
				
					bw.append(drug + "\t" + line);
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
