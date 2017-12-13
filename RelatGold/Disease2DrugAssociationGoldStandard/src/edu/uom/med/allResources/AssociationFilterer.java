package edu.uom.med.allResources;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Program filters the disease-drug association restricted to drugs with gene interaction
 * information and eliminates associations with "Biological Products" as drug.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class AssociationFilterer {

	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String arg1 = args[0]; //input file -- drug-gene association
		String arg2 = args[1]; //input file -- disease-drug association 
		String arg3 = args[2]; //output file

		String line="";
		int count=0;
		
		ArrayList<String> drugs = new ArrayList<String>();
		ArrayList<String> drugID = new ArrayList<String>();
		
		try {
			FileInputStream fis0 = new FileInputStream(arg1);
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
		    	String[] arrLine = line.split("\t");
		    	
		    	drugs.add(arrLine[0].trim().toLowerCase());
		    	drugID.add(arrLine[2].trim());
		    }
			
			FileInputStream fis = new FileInputStream(arg2);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				if(!drugs.contains(arrLine[0].trim().toLowerCase()) && !drugID.contains(arrLine[2].trim())) continue;
				if(arrLine[0].trim().equalsIgnoreCase("biological products")) continue;
				
				bw.append(line);
				bw.append("\n");

				count++;
				//if(count==10) break;
				if(count%1000==0) System.out.println(count);
			}

			br0.close();
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
