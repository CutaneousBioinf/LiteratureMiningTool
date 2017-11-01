package edu.uom.med.reactomePathways;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Program corresponds to processing of the resource from Reactome database. It is meant to regroup the 
 * processed file using Linux commands (refer Documentation/Drug_gene_pathway.docx). For every gene (from 
 * drug-gene association) the program collects a list of pathways and writes to an output file, where the
 * list of pathways are separated by '|'.   
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class GenesAndPathwaysRegrouper {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="", gene="";
		int count=0;
		
		ArrayList<String> pathwaysGenesList = new ArrayList<String>();
		
		String arg1 = args[0]; //OUTPUT_FILE_pathwayAndGenesOnly_tabdelimited_quotesremoved.txt (refer Documentaion/Drug_gene_pathway.docx)
		String arg2 = args[1]; //GENESLIST (refer Documentaion/Drug_gene_pathway.docx)
		String arg3 = args[2]; //OUTPUT_FILE_REACTOME (refer Documentaion/Drug_gene_pathway.docx)
		
		try {
			FileReader fr0 = new FileReader(arg1);
			BufferedReader br0 = new BufferedReader(fr0);
			while((line=br0.readLine()) != null) {
				pathwaysGenesList.add(line);
			}
			
			FileReader fr = new FileReader(arg2);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg3);
			BufferedWriter bw = new BufferedWriter(fw);
			while((gene=br.readLine()) != null) {
				String pathways="";
				Set<String> pathwaysList = new HashSet<String>();
				
				for(String each : pathwaysGenesList) {
					if(each.contains(gene)) {
						String[] arrEach = each.split("\t");
						ArrayList<String> genesList = new ArrayList<String>();
						
						if(arrEach.length != 2) continue;

                        if(arrEach[1].contains(";")) {
                                String[] arrGenesList = arrEach[1].split(";");
                                genesList = new ArrayList<String>(Arrays.asList(arrGenesList));
                        }
                        else genesList.add(arrEach[1]);

                        if(genesList.contains(gene)) pathwaysList.add(arrEach[0]);
					}
				}
				
				for(String eachpathway : pathwaysList) {
					if(pathways.isEmpty()) pathways = eachpathway;
					else pathways = pathways + "|" + eachpathway;
				}
				
				if(pathways.isEmpty()) pathways = "none";
				
				bw.append(gene + "\t" + pathways);
				bw.append("\n");
				
				count++;
				//if(count==10) break;
				if(count%100==0) System.out.println(count);
			}
			
			br0.close();
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
    	long elapsedTime = stopTime - startTime;
    	System.out.println("Execution time in milliseconds : " + elapsedTime);	
	}
	
}
