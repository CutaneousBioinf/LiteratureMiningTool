package edu.uom.med.geneDictionaryCreator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * The resource gene2pubmed from NCBI consists of Gene ID. However, we need Official Gene Symbol, Synonyms/Aliases and
 * Name for mapping genes in the biomedical literature. The resource Homo_sapiens.gene_info.gz from Entrez Gene
 * contains of the information we are looking for. The file can be downloaded from:  
 * ftp://ftp.ncbi.nih.gov/gene/DATA/GENE_INFO/Mammalia/. 
 * Class EntrezGeneExtractor generates a gene library by assigning gene symbol, synonyms and name for each gene ID
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class EntrezGeneExtractor {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="";
		int count=0;
		
		String arg1 = args[0]; //INPUT_FILE -- Homo_sapiens.gene_info
		String arg2 = args[1]; //OUTPUT_FILE 		
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
			
			FileOutputStream fos = new FileOutputStream(arg2);
			OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
			
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(arrLine.length>5) {
					String geneID = arrLine[1].trim();
					String geneSymbol = arrLine[2].trim();
					String geneSynonyms = arrLine[4].trim();
					String geneDescription = arrLine[8].trim();
					bw.append(geneID+"\t"+geneSymbol+"\t"+geneSynonyms+"\t"+geneDescription);
					bw.append("\n");
					count++;
				}
			}
			
			System.out.println(count);
			
			br.close();
			bw.close();
		} catch (IOException e) {
			System.err.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
}
