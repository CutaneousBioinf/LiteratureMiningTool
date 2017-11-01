package edu.uom.med.ctdProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Disease-gene association from Comparative Toxicogenomics Database (CTD) is not annotated with organism. Therefore, 
 * filtering the associations related to human is challenging. We use Homo_sapiens.gene_info resource from Entrez Gene to 
 * identify the gene symbols related to human and filter the matching records. Disease-gene association from CTD can be 
 * downloaded from http://ctdbase.org/downloads/ and Homo_sapiens.gene_info from Entrez Gene can be downloaded from 
 * ftp://ftp.ncbi.nlm.nih.gov/gene/DATA/GENE_INFO/Mammalia/
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class HumanRecordsRetriever {

	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //Homo_sapiens.gene_info (refer Documentation/Gene_disease_association.docx)
		String arg2 = args[1]; //CTD_SOURCE_FILE_selectedColumns (refer Documentation/Gene_disease_association.docx)
		String arg3 = args[2]; //CTD_SOURCE_FILE_selectedColumns_human (refer Documentation/Gene_disease_association.docx)
		
		String line="";
		
		ArrayList<String> geneSymbolsForHuman = new ArrayList<String>();
		
		try {
			//Homo_sapiens.gene_info resource from Entrez Gene
			FileInputStream fis0 = new FileInputStream(arg1);
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
				String[] arrLine = line.split("\t");
				geneSymbolsForHuman.add(arrLine[2]);
			}
		       
		    FileInputStream fis = new FileInputStream(arg2);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		    
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(geneSymbolsForHuman.contains(arrLine[0])) {
					bw.append(line);
					bw.append("\n");
				}
			}
			
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
