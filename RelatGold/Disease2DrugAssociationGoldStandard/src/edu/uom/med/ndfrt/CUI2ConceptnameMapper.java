package edu.uom.med.ndfrt;


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
 * Disease-Drug association from NDFRT is available with MRREL.RRF file within UMLS Metathesaurus. The concepts are 
 * represented only with unique concept identifier (CUI). The Program maps these CUIs back to concept name for both disease
 * and drug.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class CUI2ConceptnameMapper {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //mrconso.preferredVocabularies_CUIAndConcept (refer Documentation/Disease_drug_association.docx)
		String arg2 = args[1]; //INPUT_FILE -- OUTPUT_FILE_filtered_uniquelist (refer Documentation/Disease_drug_association.docx)
		String arg3 = args[2]; //OUTPUT_FILE -- OUTPUT_FILE1 (refer Documentation/Disease_drug_association.docx)
		
		String line="";
		int count=0;
		
		Set<String> cui = new HashSet<String>();
		HashMap<String, String> cuiAndConcept = new LinkedHashMap<String, String>();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		    while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\\|");
				
				int l1 = cui.size();
				cui.add(arrLine[0]);
				int l2 = cui.size();
				
				if(l2 > l1) cuiAndConcept.put(arrLine[0], arrLine[1]);
			}
		    System.out.println("Dictionary reading is successful!!! " + cuiAndConcept.size());
		    
		    FileInputStream fis1 = new FileInputStream(arg2);
			InputStreamReader isr1 = new InputStreamReader(fis1,"UTF-8");
		    BufferedReader br1 = new BufferedReader(isr1);
		         
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br1.readLine()) != null) {
				String[] arrLine = line.split("\\|");
				String concept1 = cuiAndConcept.get(arrLine[0]);
				String concept2 = cuiAndConcept.get(arrLine[1]);
				
				bw.append(arrLine[0] + "\t" + concept1 + "\t" + arrLine[1] + "\t" + concept2);
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
