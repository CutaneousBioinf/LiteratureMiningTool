package edu.uom.med.pharmgkbProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Program processes the downloaded relationship file from PharmGKB and extracts gene-chemical associations. The program 
 * eliminates the associations that are annotated only in literature, because these annotations are retrieved using text
 * mining approaches only. The download from PharmGKB requires a license and can be obtained by contacting the database
 * team.
 *    
 * 
 * @author Kalpana Raja
 *
 */

public class GoldStandardGenerator {
	
	/**
	 * The program execution starts from here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //relationships.tsv
		String arg2 = args[1]; //OUTPUT_FILE_PharmGKB
		
		String line="";
		ArrayList<String> associations = new ArrayList<String>();
		
		DrugnameToLexicondrugidMapper drugnameToLexicondrugidMapper = new DrugnameToLexicondrugidMapper();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);

		    FileOutputStream fos = new FileOutputStream(arg2);
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
		    BufferedWriter bw = new BufferedWriter(osw);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(arrLine[2].equals("Chemical") && arrLine[5].equals("Gene") && 
						!arrLine[6].equals("LiteratureAnnotation") && arrLine[7].equals("associated")) {
					associations.add(arrLine[1]+"\t"+arrLine[4]);
				}
			}
			
			for(String eachAssociation : associations) {
				bw.append(eachAssociation);
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
