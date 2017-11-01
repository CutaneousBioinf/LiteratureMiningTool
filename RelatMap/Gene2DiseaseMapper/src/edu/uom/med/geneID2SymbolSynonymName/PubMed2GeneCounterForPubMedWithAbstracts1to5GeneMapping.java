package edu.uom.med.geneID2SymbolSynonymName;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Program groups the genes mapping to each PMID
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class PubMed2GeneCounterForPubMedWithAbstracts1to5GeneMapping {
	
	//global attributes
	static String line="";
	static int count=0, count1=0;
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String arg1 = args[0]; //input file - gene2pubmed_Human_withAbstracts_1to5GeneMapping_sortedOnPubmed.txt
		String arg2 = args[1]; //output file - gene2pubmed_Human_withAbstracts_1to5GeneMapping_sortedOnPubmed_pubmedGenecountGenelist.txt
		
		//Count genes for every PMIDs
		pubmed2geneCount(arg1, arg2);
		
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Execution time: "+elapsedTime);
	}
	
	/**
	 * Method for counting the mapping genes to each PMID
	 * 
	 * @return 
	 */
	
	public static void pubmed2geneCount(String arg1, String arg2) {
		String pubmedID="", genes="";
		ArrayList<String> geneList = new ArrayList<String>();
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(pubmedID.isEmpty()) {
					pubmedID=arrLine[2];
					geneList.add(arrLine[1]);
					count++;
				}
				else if(!pubmedID.isEmpty() && pubmedID.equals(arrLine[2])) {
					geneList.add(arrLine[1]);
					count++;
				}
				else if(!pubmedID.isEmpty() && !pubmedID.equals(arrLine[2])) {
					genes="";
					for(String eachgene : geneList) {
						if(genes.isEmpty()) genes = eachgene;
						else genes = genes+"###"+eachgene;
					}
					
					bw.append(pubmedID+"\t"+count+"\t"+genes);
					bw.append("\n");
					
					geneList.clear();
					geneList.add(arrLine[1]);
					count=1;
					pubmedID=arrLine[2];
				}
	
				count1++;
				//if(count1==25) break; 
			}
			genes="";
			for(String eachgene : geneList) {
				if(genes.isEmpty()) genes = eachgene;
				else genes = genes+"###"+eachgene;
			}
			
			bw.append(pubmedID+"\t"+count+"\t"+genes);
			bw.append("\n");
						
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
	}	
	
}
