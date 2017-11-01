package edu.uom.med.ctdProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Comparative Toxicogenomics Database (CTD) uses MeSH ID for identifying the diseases and their synonyms. However, we are
 * interested in using the unique concept identifier (CUI) from UMLS Metathesaurus. Therefore, we developed a program to
 * map MeSH ID to CUI for diseases. While CTD uses gene symbol and gene ID to define a gene in an association, We consider 
 * only gene symbol in the association. The program filters gene symbol, disease name and disease CUI as output. 
 * 
 * @author Kalpana Raja
 *
 */

public class Conceptname2CUIMapper {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //CTD_SOURCE_FILE_selectedColumns_human (refer Documentation/Gene_disease_association.docx)
		String arg2 = args[1]; //CTD_SOURCE_FILE_selectedColumns_human_CUIForDiseases (refer Documentation/Gene_disease_association.docx)
		
		String line="";
		int count=0;
		
		Set<String> concept = new HashSet<String>();
		HashMap<String, String> conceptAndCUI = new LinkedHashMap<String, String>();
		
		try {
			//preferred concepts in English language from UMLS Metathesaurus
			FileInputStream fis = new FileInputStream("resources/lookup/mrconso.preferredVocabularies_CUIAndConcept");
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		    while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\\|");
				
				int l1 = concept.size();
				concept.add(arrLine[1]);
				int l2 = concept.size();
				
				if(l2 > l1) conceptAndCUI.put(arrLine[1], arrLine[0]);
			}
		    System.out.println("Dictionary reading is successful!!! " + conceptAndCUI.size());
		    
		    FileInputStream fis1 = new FileInputStream(arg1);
			InputStreamReader isr1 = new InputStreamReader(fis1,"UTF-8");
		    BufferedReader br1 = new BufferedReader(isr1);
		    
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			bw.append("Gene_symbol\tDisease_name\tDiseaseCUI");
			bw.append("\n");
		    while((line = br1.readLine()) != null) {
				String[] arrLine = line.split("\t");
				String diseaseCUI = conceptAndCUI.get(arrLine[2]);
				
				bw.append(arrLine[0] + "\t" + arrLine[2] + "\t" + diseaseCUI);
				bw.append("\n");
				
				count++;
				//if(count==25) break;
				if(count%500 == 0) System.out.println(count);
			}
			br.close();
			br1.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
}
