package edu.uom.med.drugsOnlyFilterer;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Class DrugsOnlyFilterer filters PubMed sentences mapped only with drugs and not chemicals. We achieve this with
 * three UMLS semantic types namely clinical drugs, antibiotics and pharmacologically active substances. 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugsOnlyFilterer {
	
	/**
	 * Program execution starts from here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		String line="";
		
		try {
			FileInputStream fis = new FileInputStream("pubmedSentences_all_genes_chemicalsAndDrugs_Mapped_output.txt");
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream("pubmedSentences_all_genes_drugs_Mapped_output.txt");
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(!arrLine[5].contains("_T195") && //Antibiotics 
						!arrLine[5].contains("_T200") && //Clinical drugs
						!arrLine[5].contains("_T121")) continue; //Pharmacologically active substances
				
				bw.append(line);
				bw.append("\n");
			}
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
}
