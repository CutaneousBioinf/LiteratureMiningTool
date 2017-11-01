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

		String line="";
		int count=0, count1=0;
		
		ArrayList<String> gene2pubmedHuman = new ArrayList<String>();
		
		try {
			FileReader fr0 = new FileReader("gene2pubmed_Human_1to5GeneMapping");
			BufferedReader br0 = new BufferedReader(fr0);
			while((line=br0.readLine()) != null) {
				gene2pubmedHuman.add(line);
			}
			
			FileReader fr = new FileReader("pubmed_with_disease_gene_mapping_43diseases");
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter("/net/psoriasis/home/rkalpana/Projects/Complex_Traits_50/Gene_Disease_Mapping/Step_2_processing/pubmed_gene_mapping_restrictedTo1to5genes_43diseases.txt");
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
		System.out.println("Execution time: "+elapsedTime);
	}
	
}
