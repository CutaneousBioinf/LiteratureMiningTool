package edu.uom.med.geneDiseaseMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * We already have list of PMIDs assigned with diseases (43 in total) and filtered PMIDs that have up to 5 genes
 * annotation in gene2pubmed file from NCBI. As a next step, we present the program below to assign both disease
 * and gene for each PMID.
 * 
 * @author kalpanaraja
 *
 */

public class GeneDiseaseMapper {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE1 -- pubmed_disease_mapping_restrictedTo1to5genes_43diseasesUnderStudy
		String arg2 = args[1]; //INPUT_FILE2 -- pubmed_gene_mapping_restrictedTo1to5genes_43diseasesUnderStudy
		String arg3 = args[2]; //OUTPUT_FILE -- pubmed_gene_disease_mapping_43diseasesUnderStudy
		
		String line="";
		int count=0, count1=0;
		
		ArrayList<String> diseaseMapping = new ArrayList<String>();
		
		try {
			FileReader fr0 = new FileReader(arg1); 
			BufferedReader br0 = new BufferedReader(fr0);
			while((line=br0.readLine()) != null) {
				diseaseMapping.add(line);
			}
			
			FileReader fr = new FileReader(arg2); 
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg3); 
			BufferedWriter bw = new BufferedWriter(fw);
			while((line=br.readLine()) != null) {
				
				String[] arrLine = line.split("\t");
				for(String each : diseaseMapping) {
					if(each.startsWith(arrLine[0])) {
						String[] arrEach = each.split("\t");
						bw.append(line+"\t"+arrEach[1]);
						bw.append("\n");
						
						count1++;
					}
				}
				
				count++;
				//if(count==100) break;
			}
			
			System.out.println(count);
			System.out.println(count1);
			br0.close();
			br.close();
			bw.close();
		} catch (IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
      	long elapsedTime = stopTime - startTime;
      	System.out.println("Execution time in milliseconds: "+elapsedTime);
	}
	
}
