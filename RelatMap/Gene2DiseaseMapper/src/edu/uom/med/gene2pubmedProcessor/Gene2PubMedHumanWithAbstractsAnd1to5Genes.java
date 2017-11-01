package edu.uom.med.gene2pubmedProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * The program is meant to filter gene2pubmed records with mapping of up to 5 genes per PubMed ID. The project requires
 * an input file with gene2pubmed records related to human and has abstract. Please refer to 
 * Documentation/Processing_of_gene2pubmed.docx for filtering records related to human and has abstract.
 * 
 *
 * @author Kalpana Raja
 *
 */

public class Gene2PubMedHumanWithAbstractsAnd1to5Genes {
	
	/**
	 * Program execution starts here.
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="";
		int count=0;
		
		String arg1 = args[0]; //INPUT_FILE1 -- gene2pubmed_human_hasAbstract_PMIDMapsTo1to5genes
		String arg2 = args[1]; //INPUT_FILE2 -- gene2pubmed_human
		String arg3 = args[2]; //OUTPUT_FILE 
		
		ArrayList<String> pmids = new ArrayList<String>();
		
		final long startTime = System.currentTimeMillis();
		
		try {
			FileReader fr0 = new FileReader(arg1);
			BufferedReader br0 = new BufferedReader(fr0);
			while((line = br0.readLine()) != null) {
				pmids.add(line.trim());
			}
			
			FileReader fr = new FileReader(arg2);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg3);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(pmids.contains(arrLine[2].trim())) { 
					bw.append(line);
					bw.append("\n");
				}
				
				count++;
				//if(count==100) break;
				if(count%100000==0) System.out.println(count);
			}
			
			final long endTime = System.currentTimeMillis();
			System.out.println("Total execution time: " + (endTime - startTime));
			
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
