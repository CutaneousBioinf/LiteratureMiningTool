package edu.nu.nlp.preProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The entries in gene2pubmed resource are pairs of gene and PMID. This means that each gene could map to multiple 
 * PMIDs and each PMID could map up to 5 genes (we use this criteria in the present study). Class PubMed2GeneList
 * groups all the genes for each PMID. 
 * 
 * @author Kalpana Raja
 *
 */

public class PubMed2GeneList {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		String line="", pmid="";
		
		ArrayList<String> gene2pubmedHuman = new ArrayList<String>();
		
		try {
			FileReader fr0 = new FileReader("C:/Users/Owner/Desktop/Novel Drug Discovery/Gene_Disease_Mapping_Literature/Step_1_Citations_Retrieval/gene2pubmed_Human");
			BufferedReader br0 = new BufferedReader(fr0);
			while((line = br0.readLine()) != null) {
				gene2pubmedHuman.add(line);
			}
			
			FileReader fr = new FileReader("C:/Users/Owner/Desktop/Novel Drug Discovery/Gene_Drug_Mapping_Literature/Step_1_citation_retrieval/gene2pubmed_Human_PMIDs.txt");
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter("C:/Users/Owner/Desktop/Novel Drug Discovery/Gene_Drug_Mapping_Literature/Step_1_citation_retrieval/pubmed_geneList");
			BufferedWriter bw = new BufferedWriter(fw);
			while((pmid = br.readLine()) != null) {
				ArrayList<String> geneList = new ArrayList<String>();
				for(String each : gene2pubmedHuman) {
					if(each.contains(pmid)) {
						String[] arrEach = each.split("\t");
						geneList.add(arrEach[1]);
					}
				}
				if(!geneList.isEmpty()) {
					bw.append(pmid+"\t"+geneList);
					bw.append("\n");
				}
			}
			
			br0.close();
			br.close();
			bw.close();
		}catch(IOException e) {
			System.err.println(e);
		}
	}
	
}
