package edu.uom.med.geneID2SymbolSynonymName;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Program replaces gene ID with Gene Symbol$$$Synonym$$$Name in the processed file from gene2pubmed resource. 
 * 
 * @author Kalpana Raja
 *
 */

public class GeneID2SymbolSynonymName {
	
	/**
	 * Program execution start here
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="";
		int count=0;
		
		String arg1 = args[0]; //INPUT_FILE1 - geneSymbolSynonyms_preprocessed
		String arg2 = args[1]; //INPUT_FILE2 - gene2pubmed_Human_withAbstracts_1to5GeneMapping_sortedOnPubmed_pubmedGenecountGenelist.txt
		String arg3 = args[2]; //OUTPUT_FILE - gene2pubmed_Human_withAbstracts_1to5GeneMapping_sortedOnPubmed_pubmedGenecountGenelist_ pubmedGenecountGenenamelist.txt
		
		LinkedHashMap<String, String> geneInfo = new LinkedHashMap<String, String>(59930);
		
		try {
			FileReader fr0 = new FileReader(arg1);
			BufferedReader br0 = new BufferedReader(fr0);
			while((line = br0.readLine()) != null) {
				String[] arrLine = line.split("\t");
				geneInfo.put(arrLine[0], arrLine[1]);
			}
			
			FileReader fr = new FileReader(arg2);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg3);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine()) != null) {
				ArrayList<String> geneID = new ArrayList<String>();
				ArrayList<String> geneName = new ArrayList<String>();
				
				String[] arrLine = line.split("\t");
				if(arrLine[2].contains("###")) {
					String[] geneIDList = arrLine[2].split("###");
					geneID = new ArrayList<String>(Arrays.asList(geneIDList));
				}
				else geneID.add(arrLine[2]);
				
				for(String eachID : geneID) {
					String geneSymbolSynonymName = geneInfo.get(eachID);
					geneName.add(geneSymbolSynonymName);
				}
				
				String geneInfoList="";
				for(String each : geneName) {
					if(geneInfoList.isEmpty()) geneInfoList = each;
					else geneInfoList = geneInfoList+" ### "+each;
				}
				
				bw.append(arrLine[0]+"\t"+arrLine[1]+"\t"+geneInfoList);
				bw.append("\n");
				
				count++;
				//if(count==5) break;
				if(count%10000==0) System.out.println(count);
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
