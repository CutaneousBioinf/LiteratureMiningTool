package edu.uom.med.gseaPathways;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Program to include ":GO" label for functions from CGAP and GSEA
 * 
 * @author Kalpana Raja
 *
 */
public class GOFunctionAnnotator {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String line="";
		int count=0;
		
		ArrayList<String> go = new ArrayList<String>();
		
		String arg1 = args[0]; //GSEA_OUTPUT_FILE_functions (refer Documention/Drug_gene_pathway.docx)
		String arg2 = args[1]; //OUTPUT_FILE_all_resources (refer Documention/Drug_gene_pathway.docx)
		String arg3 = args[2]; //OUTPUT_FILE_goLabel (refer Documention/Drug_gene_pathway.docx)
		
		try {
			//GO annotation from Biocarta and GSEA
			FileInputStream fis0 = new FileInputStream(arg1);
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
				go.add(line);
			}
			
			FileInputStream fis = new FileInputStream(arg2);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				if(go.contains(line)) {
					bw.append(line + ":GO");
					bw.append("\n");
				}
				else {
					bw.append(line);
					bw.append("\n");
				}
				
				count++;
				if(count==1000) break;
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
