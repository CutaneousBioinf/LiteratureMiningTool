package edu.uom.med.geneDiseaseMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * We are interested to get PMIDs annotated with up to 5 genes. The program PubMedDiseaseMapper1.java filters the 
 * PMIDs annotated with at least one complex disease and annotated with up to 5 genes.
 * 
 * @author Kalpana Raja
 *
 */

public class PubMedDiseaseMapper1 {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="";
		int count=0, count1=0;
		
		ArrayList<String> pubmed2disease = new ArrayList<String>();
		
		try {
			FileReader fr0 = new FileReader("pubmed_disease_mapping_43diseases.txt"); //43 diseases
			BufferedReader br0 = new BufferedReader(fr0);
			while((line=br0.readLine()) != null) {
				pubmed2disease.add(line);
			}
			
			FileReader fr = new FileReader("pubmed_with_disease_gene_mapping_43diseases"); //43 diseases
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter("/net/psoriasis/home/rkalpana/Projects/Complex_Traits_50/Gene_Disease_Mapping/Step_2_processing/pubmed_disease_mapping_restrictedTo1to5genes_43diseases.txt"); //43 diseases
			BufferedWriter bw = new BufferedWriter(fw);
			while((line=br.readLine()) != null) {
				for(String each : pubmed2disease) {
					if(each.contains(line)) {
						String[] arrEach = each.split("\t");
						if(arrEach[0].trim().equals(line.trim())) {
							String disease = arrEach[1];
							//System.out.println(line+"\t"+disease);
							bw.append(line+"\t"+disease);
							bw.append("\n");
						
							count1++;
						}
					}
				}
				count++;
				if(count%500==0) System.out.println("lines processed: "+count);
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
