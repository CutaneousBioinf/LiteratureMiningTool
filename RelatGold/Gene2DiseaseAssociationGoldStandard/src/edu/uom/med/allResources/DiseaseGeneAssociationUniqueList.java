package edu.uom.med.allResources;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Program retrieves a unique list of gene-disease association from a combined file (associations from CTD and PharmGKB).
 * The list is based on gene symbol and disease CUI.
 * 
 *
 * @author Kalpana Raja
 *
 */

public class DiseaseGeneAssociationUniqueList {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; // OUTPUT_FILE (refer Documention/Gene_disease_association.docx)
		String arg2 = args[1]; // OUTPUT_FILE_uniquelist	

		String line="";
		int count=0;
		
		Set<String> associations = new LinkedHashSet<String>();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
			FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				int length1 = associations.size();
				associations.add(arrLine[0] + "\t" + arrLine[2]);
				int length2 = associations.size();
				
				if(length2 > length1) {
					bw.append(line);
					bw.append("\n");
				}

				count++;
				//if(count==25) break;	
				if(count%100 == 0) System.out.println(count);
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
