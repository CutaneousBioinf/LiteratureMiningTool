package edu.uom.med.conceptsDictionaryGenerator;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/** 
 * The MRSTYCuiTuiGenerator program implements an application that extracts and groups the unique 
 * concept identifier (CUI), semantic type identifier (TUI), and semantic type of each concept 
 * in the MRSTY.RRF file within UMLS Metathesaurus. 
 * 
 * @author Kalpana Raja and Troy Cao
 *
 */

public class MRSTYCuiTuiGenerator {
	
	/**
	 * Method reads and filters CUI TUI pairs from MRSTY.RRF
	 * 
	 * @throws IOException
	 */
	
	public void cuiTuiGenerator(String mrstyFile) throws IOException {
		
		System.out.println("Processing MRSTY within UMLS Metathesaurus....");
		
		//To read MRSTY.RRF within UMLS Metathesaurus 2016AA version
		BufferedReader in = new BufferedReader(new FileReader(mrstyFile));
		PrintWriter pwr = new PrintWriter("resources/secondaryDictionaries/mrsty.cuitui2016AA.txt");
		
		ArrayList<String> terms = new ArrayList<String>();
		int count=0;
		
		while(in.ready()){
			String line = in.readLine();
			String[] splits=line.split("\\|");
			String cui = splits[0];
			String tui = splits[1];
			String type = splits[3];
			String cui_tui = cui.concat("_").concat(tui);
			
			terms.add(cui_tui.concat("\t").concat(type));
			
			count++;
			if(count%1000==0) System.out.println("Processed " + count + " so far");
		}
		in.close();
		
		for (String t : terms) {
			pwr.println(t); 
		}
		pwr.close();
		
		System.out.println("Total number of records: "+count);
	}
	
}
