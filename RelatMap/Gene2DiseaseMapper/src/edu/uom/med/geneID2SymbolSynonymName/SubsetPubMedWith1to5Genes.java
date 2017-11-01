package edu.uom.med.geneID2SymbolSynonymName;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Program filters PMIDs mapped with up to five genes only. Genes are degined by their symbol, synonyms and name
 * 
 * @author Kalpana Raja
 *
 */

public class SubsetPubMedWith1to5Genes {
	
	/**
	 * Program execution starts from here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="";
		int count=0;
		
		String arg1 = args[0]; //INPUT_FILE1 - pubmedGeneSymbolSynonymName.txt
		String arg2 = args[1]; //INPUT_FILE2 - PubMedWith1to5Genes.txt
		String arg3 = args[2]; //OUTPUT_FILE - pubmedGeneSymbolSynonymName1to5.txt
		
		ArrayList<String> geneInfo = new ArrayList<String>();
		ArrayList<String> pubmedList = new ArrayList<String>();
		ArrayList<String> geneInfo1to5 = new ArrayList<String>();
		
		try {
			FileReader fr0 = new FileReader(arg1);
			BufferedReader br0 = new BufferedReader(fr0);
			while((line = br0.readLine()) != null) {
				geneInfo.add(line.trim());
			}
			
			FileReader fr = new FileReader(arg2);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg3);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine()) != null) {
				pubmedList.add(line.trim());
				
				count++;
				//if(count==10) break;
				if(count%10000==0) System.out.println(count);
			}
		
			for(String eachID : pubmedList) {
				innerLoop:
				for(String each : geneInfo) {
					if(each.startsWith(eachID+"\t")) {
						geneInfo1to5.add(each);
						break innerLoop;
					}
				}
			}
			
			for(String eachRecord : geneInfo1to5) {
				bw.append(eachRecord);
				bw.append("\n");
			}
			
			br0.close();
			br.close();
			bw.close();
		}catch(IOException e) {
			System.err.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
}
