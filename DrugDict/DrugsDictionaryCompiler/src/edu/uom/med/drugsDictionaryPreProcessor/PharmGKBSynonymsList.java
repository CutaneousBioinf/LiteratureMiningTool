package edu.uom.med.drugsDictionaryPreProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Certain synonyms in PharmGKB contain additional information within a pair of braces and are placed next to the 
 * drug name. However, this is not useful while performing synonym matching between the resources. Therefore, we 
 * developed a program called PharmGKBSynonymsList.java to remove the suffix with details within a pair of braces in 
 * DrugBank drug names. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class PharmGKBSynonymsList {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- pharmgkb_drugs_synonyms_output
		String arg2 = args[1]; //OUTPUT_FILE -- pharmgkbSynonyms.txt

		String line="";
		
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				//remove ending semantic tag such as (disorder) or [Ambiguity] for better matching
				if(arrLine[1].endsWith(")")&&arrLine[1].matches(".+\\s+\\(\\S+\\)"))  arrLine[1]=arrLine[1].substring(0, arrLine[1].lastIndexOf("("));
				else if(arrLine[1].endsWith("]")&&arrLine[1].matches(".+\\s+\\[\\S+\\]")) arrLine[1]=arrLine[1].substring(0, arrLine[1].lastIndexOf("["));
				String drug = arrLine[0]+"###"+arrLine[1];
				
				if(arrLine.length==3) {
					if(!arrLine[2].isEmpty()) {
						if(arrLine[2].endsWith(")")&&arrLine[2].matches(".+\\s+\\(\\S+\\)"))  arrLine[2]=arrLine[2].substring(0, arrLine[2].lastIndexOf("("));
						else if(arrLine[2].endsWith("]")&&arrLine[2].matches(".+\\s+\\[\\S+\\]")) arrLine[2]=arrLine[2].substring(0, arrLine[2].lastIndexOf("["));
						drug = drug+"###"+arrLine[2];
					}
				}
				if(arrLine.length==4) {
					if(!arrLine[2].isEmpty()) {
						if(arrLine[2].endsWith(")")&&arrLine[2].matches(".+\\s+\\(\\S+\\)"))  arrLine[2]=arrLine[2].substring(0, arrLine[2].lastIndexOf("("));
						else if(arrLine[2].endsWith("]")&&arrLine[2].matches(".+\\s+\\[\\S+\\]")) arrLine[2]=arrLine[2].substring(0, arrLine[2].lastIndexOf("["));
						drug = drug+"###"+arrLine[2];
					}
					if(!arrLine[3].isEmpty()) {
						if(arrLine[3].endsWith(")")&&arrLine[3].matches(".+\\s+\\(\\S+\\)"))  arrLine[3]=arrLine[3].substring(0, arrLine[3].lastIndexOf("("));
						else if(arrLine[3].endsWith("]")&&arrLine[3].matches(".+\\s+\\[\\S+\\]")) arrLine[3]=arrLine[3].substring(0, arrLine[3].lastIndexOf("["));
						drug = drug+"###"+arrLine[3];
					}
				}
				bw.append(drug);
				bw.append("\n");
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
