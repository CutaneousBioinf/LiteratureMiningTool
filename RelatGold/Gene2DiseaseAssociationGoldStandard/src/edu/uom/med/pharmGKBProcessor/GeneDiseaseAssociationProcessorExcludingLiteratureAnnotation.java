package edu.uom.med.pharmGKBProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Program is meant to process the relationships resource file downloaded from PharmGKB. The file is not available for free
 * and requires a license that can be obtained by contacting PharmGKB database. The program filters gene-disease
 * associations from PharmGKB (excluding the associations only from literature).
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class GeneDiseaseAssociationProcessorExcludingLiteratureAnnotation {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //relationships.tsv
		String arg2 = args[1]; //OUTPUT_FILE_literatureAnnotationExcluded
		
		String line="";
		int count=0;
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				if(arrLine[2].equals("Gene") && arrLine[5].equals("Disease") && 
						!arrLine[6].equals("LiteratureAnnotation") && arrLine[7].equals("associated")) {
					bw.append(arrLine[0]+"\t"+arrLine[1]+"\t"+arrLine[2]+"\t"
							+arrLine[3]+"\t"+arrLine[4]+"\t"+arrLine[5]+"\t"
							+arrLine[7]);
					bw.append("\n");
				}
				
				count++;
				//if(count==50) break;
				if(count%10000==0) System.out.println(count);
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
