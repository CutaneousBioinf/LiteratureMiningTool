package edu.uom.med.gseaPathways;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Program to extract genes and gene ontology from C5 version of GSEA
 * 
 * @author Kalpana Raja
 *
 */

public class GenesGOMiner {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="", resource="", goAnnotation="";
		
		String arg1 = args[0]; //c5.all.v6.0.symbols.gmt (refer Documentation/Drug_gene_pathway.docx)
		String arg2 = args[1]; //OUTPUT_FILE
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line=br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				resource = arrLine[0].substring(0, arrLine[0].indexOf("_"));
				goAnnotation = arrLine[0].substring(arrLine[0].indexOf("_")+1);
				goAnnotation = goAnnotation.replaceAll("_", " ");
				
				for(int i=2; i<arrLine.length; i++) {
					bw.append(resource + "\t" + arrLine[i] + "\t" + goAnnotation);
					bw.append("\n");
				}
				
				resource = ""; 
				goAnnotation = "";
			}
			
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
    	long elapsedTime = stopTime - startTime;
    	System.out.println("Execution in milliseconds: " + elapsedTime);	
	}
	
}
