package edu.uom.med.pathwayNormalizer;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Program removes the preceding and succeeding white spaces   
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DuplicateRecordsRemover {

	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="";
		int count=0;
		
		String arg1 = args[0]; //input_file
		String arg2 = args[1]; //output_file
		
		ArrayList<String> data = new ArrayList<String>();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				String record = arrLine[0].trim() + "\t" + arrLine[1].trim();
				
				if(!data.contains(record)) data.add(record);
					
				count++;
				if(count==100) break;
				//if(count%1000==0) System.out.println(count);
			}
			
			for(String eachdata : data) {
				bw.append(eachdata);
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
