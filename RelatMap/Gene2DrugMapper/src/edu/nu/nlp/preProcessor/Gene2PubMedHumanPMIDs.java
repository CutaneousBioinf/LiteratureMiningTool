package edu.nu.nlp.preProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class collects PMIDs related to human from gene2pubmed resource from NCBI
 * 
 * @author kalpanaraja
 *
 */

public class Gene2PubMedHumanPMIDs {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("C:/Users/Owner/Desktop/Novel Drug Discovery/Gene_Disease_Mapping_Literature/Step_1_Citations_Retrieval/gene2pubmed_Human"));
			Set<String> lines = new LinkedHashSet<String>(3000000); // maybe should be bigger
			for (String line; (line = reader.readLine()) != null;) {
				String[] arrLine = line.split("\t");
			    lines.add(arrLine[2].trim()); //does nothing when record is present
			}
			reader.close();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/Owner/Desktop/Novel Drug Discovery/Gene_Drug_Mapping_Literature/Step_1_citation_retrieval/gene2pubmed_Human_PMIDs.txt"));
			for (String unique : lines) {
				writer.write(unique);
				writer.newLine();
			}
			writer.close();
			
			System.out.println(lines.size());
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
}