package edu.uom.med.geneDiseaseMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/** 
 * The program assigns genes to each PMID retrieved for 43 complex diseases under study. We use gene2pubmed file
 * from NCBI for gene annotation information.  
 *   
 * @author Kalpana Raja
 *
 */

public class PubMedGeneMapper {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE1 -- gene2pubmed_Human_1to5GeneMapping
		String arg2 = args[1]; //INPUT_FILE2 -- pubmed_with_disease_gene_mapping_43diseasesUnderStudy
		String arg3 = args[2]; //OUTPUT_FILE -- pubmed_gene_mapping_restrictedTo1to5genes_43diseasesUnderStudy

		String line="";
		int count=0, count1=0;
		
		ArrayList<String> gene2pubmedHuman = new ArrayList<String>();
		
		try {
			FileReader fr0 = new FileReader(arg1);
			BufferedReader br0 = new BufferedReader(fr0);
			while((line=br0.readLine()) != null) {
				gene2pubmedHuman.add(line);
			}
			
			FileReader fr = new FileReader(arg2);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg3);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line=br.readLine()) != null) {
				for(String each : gene2pubmedHuman) {
					if(each.contains(line)) {
						String[] arrEach = each.split("\t");
						if(arrEach[2].trim().equals(line.trim())) {
							String gene = arrEach[1];
							
							bw.append(line+"\t"+gene);
							bw.append("\n");
							count1++;
						}
					}
				}
				count++;
				if(count%10000==0) System.out.println(count);
				//if(count==100) break;
			}
			
			System.out.println(count);
			System.out.println(count1);
	
			br0.close();
			br.close();
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
