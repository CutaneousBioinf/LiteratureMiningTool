package edu.uom.med.geneID2SymbolSynonymName;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Program to get a list of PMIDs with mapping gene symbols
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class PubMed2GeneSymbols {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="";
		int count=0;
		
		String arg1 = args[0]; //INPUT_FILE - pubmedGeneSymbolSynonymName.txt
		String arg2 = args[1]; //OUTPUT_FILE - pubmedGeneSymbol.txt
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line=br.readLine()) != null) {
				ArrayList<String> symbols = new ArrayList<String>();
				
				String[] arrLine = line.split("\t");
				String line1 = arrLine[1].substring(1, arrLine[1].length()-1);
				if(line1.contains(", ")) {
					String[] arrEach = line1.split(", ");
					for(int i=0; i<arrEach.length; i++) {
						String[] eachGene = arrEach[i].split("\\$\\$");
						symbols.add(eachGene[0]);
					}
				}
				else {
					String[] arrLine1 = line1.split("\\$\\$");
					symbols.add(arrLine1[0]);
				}
				
				String symbolsList="";
				for(String eachSymbol : symbols) {
					if(symbolsList=="") symbolsList = eachSymbol;
					else symbolsList = symbolsList+"|"+eachSymbol;
				}
				bw.append(arrLine[0]+"\t"+symbolsList);
				bw.append("\n");
				
				count++;
				//if(count==4) break;
			}
			br.close();
			bw.close();
		}catch(IOException e) {
			System.out.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Execution time in milliseconds: "+elapsedTime);
	}

}
