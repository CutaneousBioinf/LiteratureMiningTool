package edu.uom.med.geneID2SymbolSynonymName;


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
 * While Class EntrezGeneExtractor generates a gene library by assigning gene symbol, synonyms and name for each 
 * gene ID, Class GeneID2SymbolSynonymNamePreprocessor process the output from Class EntrezGeneExtractor to group 
 * information in a specific format (i.e. Symbol$$$Synonyms$$$Description/Name) for each gene ID. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class GeneID2SymbolSynonymNamePreprocessor {
	
	/**
	 * Program executions starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="", name="";
		int count=0;
		
		String arg1 = args[0]; //INPUT_FILE - geneSymbolSynonyms-Entrez
		String arg2 = args[1]; //OUTPUT_FILE - geneSymbolSynonymsProcessed
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				String id = arrLine[0];
				for(int i=1; i< arrLine.length; i++) {
					if(name.isEmpty()) name = arrLine[i];
					else name = name+"$$$"+arrLine[i];
				}
				
				bw.append(id+"\t"+name);
				bw.append("\n");
				
				name="";
				
				count++;
				//if(count==10) break;
				if(count%10000==0) System.out.println(count);
			}
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
