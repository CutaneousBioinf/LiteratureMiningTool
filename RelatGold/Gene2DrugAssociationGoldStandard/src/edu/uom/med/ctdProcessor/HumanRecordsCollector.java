package edu.uom.med.ctdProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Program filters the records related to human from CTD source file
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class HumanRecordsCollector {

	/**
	 * Program executions starts here
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //OUTPUT_FILE_noIntroSection (refer Documentation/Drug_gene_association.docx)
		String arg2 = args[1]; //OUTPUT_FILE_noIntroSection_human (refer Documentation/Drug_gene_association.docx)

		String line="";
		int count=0;
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				if(line.contains("9606") && line.contains("Homo sapiens")) {
					bw.append(line);
					bw.append("\n");
				}

				count++;
				//if(count==5) break;
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
