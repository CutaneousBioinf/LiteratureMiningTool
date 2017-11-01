package edu.uom.med.genesAndPathways;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Program retrieves gene-pathway information from all resources
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class GenesAndPathwaysRetriever {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="", gene="";
		int count=0;
		
		ArrayList<String> reactomeGenes = new ArrayList<String>();
		ArrayList<String> cgapGenes = new ArrayList<String>();
		
		HashMap<String, String> reactome = new LinkedHashMap<String, String>();
		HashMap<String, String> cgap = new LinkedHashMap<String, String>();
		
		String arg1 = args[0]; //REATOME_GENES (refer Documentation/Drug_gene_pathway.docx)
		String arg2 = args[1]; //BIOCARTA_GENES (refer Documentation/Drug_gene_pathway.docx)
		String arg3 = args[2]; //OUTPUT_FILE_REACTOME (refer Documentation/Drug_gene_pathway.docx)
		String arg4 = args[3]; //OUTPUT_FILE_BIOCARTA_genesWithPathwayOrFunctionOnly (refer Documentation/Drug_gene_pathway.docx)
		String arg5 = args[4]; //GENES_FROM_REACTOME_BIOCARTA (refer Documentation/Drug_gene_pathway.docx)
		String arg6 = args[5]; //GENES_AND_PATHWAYS_FROM_REACTOME_BIOCARTA (refer Documentation/Drug_gene_pathway.docx)
		
		try {
			FileReader fr0 = new FileReader(arg1);
			BufferedReader br0 = new BufferedReader(fr0);
			while((line=br0.readLine()) != null) {
				reactomeGenes.add(line);
			}
			
			FileReader fr1 = new FileReader(arg2);
			BufferedReader br1 = new BufferedReader(fr1);
			while((line=br1.readLine()) != null) {
				cgapGenes.add(line);
			}
			
			FileReader fr2 = new FileReader(arg3);
			BufferedReader br2 = new BufferedReader(fr2);
			while((line=br2.readLine()) != null) {
				String[] arrLine = line.split("\t");
				reactome.put(arrLine[0], arrLine[1]);
			}
			
			FileReader fr3 = new FileReader(arg4);
			BufferedReader br3 = new BufferedReader(fr3);
			while((line=br3.readLine()) != null) {
				String[] arrLine = line.split("\t");
				cgap.put(arrLine[0], arrLine[1]);
			}
			
			FileReader fr = new FileReader(arg5);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg6);
			BufferedWriter bw = new BufferedWriter(fw);
			while((gene=br.readLine()) != null) {
				String pathways="", pathways_reactome="", pathways_cgap="";
				
				if(reactomeGenes.contains(gene)) pathways_reactome = reactome.get(gene);
				if(cgapGenes.contains(gene)) pathways_cgap = cgap.get(gene);
				
				Set<String> allPathways = new HashSet<String>();
				Set<String> allPathways_ignorecase = new HashSet<String>();
				if(!pathways_reactome.isEmpty()) {
					String[] pr = pathways_reactome.split("\\|");
					
					for(int i=0; i< pr.length; i++) {
						if(!allPathways_ignorecase.contains(pr[i].toLowerCase())) {
							allPathways.add(pr[i]);
							allPathways_ignorecase.add(pr[i].toLowerCase());
						}
					}
				}
				if(!pathways_cgap.isEmpty()) {
					String[] pr = pathways_cgap.split("\\|");
					
					for(int i=0; i< pr.length; i++) {
						if(!allPathways_ignorecase.contains(pr[i].toLowerCase())) {
							allPathways.add(pr[i]);
							allPathways_ignorecase.add(pr[i].toLowerCase());
						}
					}
				}
				
				for(String eachPathway : allPathways) {
					if(pathways.isEmpty()) pathways = eachPathway;
					else pathways = pathways + "|" + eachPathway;
				}
				
				bw.append(gene + "\t" + pathways);
				bw.append("\n");
				
				count++;
				if(count==10) break;
				//if(count%100==0) System.out.println(count);
			}
			
			br0.close();
			br1.close();
			br2.close();
			br3.close();
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
