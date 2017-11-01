package edu.uom.med.ctdProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Program removes the introduction section in the source file downloaded from Comparative Toxicogenomics Database (CTD)
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class Preprocessor {

	/**
	 * Program executions starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- downloaded_file_from_CTD_database (refer Documentation/Gene_disease_association.docx)
		String arg2 = args[1]; //OUTPUT_FILE_noIntroSection (refer Documentation/Gene_disease_association.docx)
		
		String line="";
		
		boolean flag=false;
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
		    while((line = br.readLine()) != null) {
				if(line.startsWith("# Fields")) flag = true;
				if(flag) {
					if(line.equals("#")) continue;
						
					bw.append(line);
					bw.append("\n");
				}
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
