package edu.uom.med.geneDiseaseMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Gene annotation in gene2pubmed file from NCBI are given as gene ID. However, we are interested to use gene symbol. 
 * Therefore, we present the program below to convert gene IDs to gene symbols. We used Entrez gene as a resource
 * to map between gene ID and gene symbol.
 * 
 * @author Kalpana Raja
 *
 */

public class GeneIDtoSymbolMapping {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE1 -- Homo_sapiens.gene_info
		String arg2 = args[1]; //INPUT_FILE2 -- pubmed_gene_disease_mapping_43diseasesUnderStudy
		String arg3 = args[2]; //OUTPUT_FILE -- pubmed_geneID_symbol_disease

		String line="";
		
		HashMap<String, String> geneIDSymbol = new LinkedHashMap<String, String>();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    	BufferedReader br = new BufferedReader(isr);
		         
		    	while((line=br.readLine()) != null) {
				if(line.startsWith("9606")) { //taxonomy for human
					String[] arrLine = line.split("\t");
					geneIDSymbol.put(arrLine[1], arrLine[2]);
				}
			}
			
			FileInputStream fis1 = new FileInputStream(arg2);
			InputStreamReader isr1 = new InputStreamReader(fis1,"UTF-8");
		    BufferedReader br1 = new BufferedReader(isr1);
		    
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);

		    while((line=br1.readLine()) != null) {
				String[] arrLine = line.split("\t");
				String geneSymbol = geneIDSymbol.get(arrLine[1]);
				bw.append(arrLine[0] + "\t" + arrLine[1] + "\t" + geneSymbol + "\t" + arrLine[2]);
				bw.append("\n");
			}
		    
			br.close();
			br1.close();
			bw.close();
		} catch (IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
      	long elapsedTime = stopTime - startTime;
      	System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
}
