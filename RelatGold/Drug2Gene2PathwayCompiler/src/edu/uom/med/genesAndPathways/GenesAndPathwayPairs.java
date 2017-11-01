package edu.uom.med.genesAndPathways;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Program to get the output as gene-pathway/functions pairs from the processed Reactome and Biocarta files 
 * 
 * @author Kalpana Raja
 *
 */

public class GenesAndPathwayPairs {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="";
		
		String arg1 = args[0]; //GENES_AND_PATHWAYS_FROM_REACTOME_BIOCARTA (refer Documentation/Drug_gene_pathway.docx)
		String arg2 = args[1]; //GENE_PATHWAY_PAIRS_FROM_REACTOME_BIOCARTA (refer Documentation/Drug_gene_pathway.docx)
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line=br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				Set<String> pathways = new HashSet<String>();
				
				if(arrLine[1].contains("|")) {
					String[] pathwaylist = arrLine[1].split("\\|");
					for(int i=0; i<pathwaylist.length; i++) {
						pathways.add(pathwaylist[i]);
					}
				}
				else {
					pathways.add(arrLine[1]);
				}
				
				for(String pathway : pathways) {
					bw.append(arrLine[0] + "\t" + pathway);
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
    	System.out.println("Execution in milliseconds: " + elapsedTime);
	}
	
}
