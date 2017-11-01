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

		String arg1 = args[0]; //INPUT_FILE1 -- pubmed_disease_mapping_43diseasesUnderStudy
		String arg2 = args[1]; //INPUT_FILE2 -- pubmed_with_disease_gene_mapping_43diseasesUnderStudy
		String arg3 = args[2]; //OUTPUT_FILE -- pubmed_disease_mapping_restrictedTo1to5genes_43diseasesUnderStudy


		String line="";
		int count=0, count1=0;
		
		ArrayList<String> pubmed2disease = new ArrayList<String>();
		
		try {
			FileReader fr0 = new FileReader(arg1); 
			BufferedReader br0 = new BufferedReader(fr0);
			while((line=br0.readLine()) != null) {
				pubmed2disease.add(line);
			}
			
			FileReader fr = new FileReader(arg2); 
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg3);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line=br.readLine()) != null) {
				for(String each : pubmed2disease) {
					if(each.contains(line)) {
						String[] arrEach = each.split("\t");
						if(arrEach[0].trim().equals(line.trim())) {
							String disease = arrEach[1];
							
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
		System.out.println("Execution time in milliseconds: "+elapsedTime);
	}
	
}
