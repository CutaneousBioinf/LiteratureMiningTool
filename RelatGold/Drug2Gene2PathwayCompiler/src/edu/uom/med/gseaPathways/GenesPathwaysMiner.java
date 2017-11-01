package edu.uom.med.gseaPathways;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Program to mine pathway information from GSEA resource file
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class GenesPathwaysMiner {
	
	/**
	 * Program executions starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="", resource="", pathway="";
		
		String arg1 = args[0]; //c2.cp.v6.0.symbols.gmt (refer Documentation/Drug_gene_pathway.docx)
		String arg2 = args[1]; //GSEA_OUTPUT_FILE_pathways (refer Documentation/Drug_gene_pathway.docx)
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line=br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				if(!arrLine[0].startsWith("WNT_SIGNALING")) resource = arrLine[0].substring(0, arrLine[0].indexOf("_"));
				
				if(arrLine[0].equals("WNT_SIGNALING")) pathway = "WNT_SIGNALING_PATHWAY";
				else {
					pathway = arrLine[0].substring(arrLine[0].indexOf("_")+1);
					pathway = pathway.replaceAll("_", " ");
				}
				
				for(int i=2; i<arrLine.length; i++) {
					bw.append(resource + "\t" + arrLine[i] + "\t" + pathway);
					bw.append("\n");
				}
				
				resource = ""; 
				pathway = "";
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
