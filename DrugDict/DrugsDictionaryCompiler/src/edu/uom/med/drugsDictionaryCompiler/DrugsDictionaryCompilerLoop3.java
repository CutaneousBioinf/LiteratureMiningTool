package edu.uom.med.drugsDictionaryCompiler;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Program is meant to identify the drugs that are present only in PharmGKB among the three resources and to collect the 
 * synonyms for those drugs from PharmGKB. This is the third step of the chemicals/drugs lexicon generation.
 * 
 * 
 * @author Kalpana Raja
 *
 */
public class DrugsDictionaryCompilerLoop3 {
	
	/**
	 * Program execution starts from here.
	 * 
	 * @param args
	 * @throws IOException
	 */
	
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- PharmGKBDrugsForLoop3
		String arg2 = args[1]; //OUTPUT_FILE -- drugsDictionaryLoop3

		String line="", drugID="", umlsSynonyms="", drugBankSynonyms="",pharmGKBSynonyms="", cuiTui="", drugBankID="", pharmGKBID="";
		
		ArrayList<String> pharmGKBList = new ArrayList<String>();
		
		FileInputStream fis = new FileInputStream(arg1);
		InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		BufferedReader br = new BufferedReader(isr);
		while((line = br.readLine()) != null) {
		    pharmGKBList.add(line);
	    }
		
		FileOutputStream fos = new FileOutputStream(arg2);
	    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
	    BufferedWriter bw = new BufferedWriter(osr);
	    
	    //get unique id
	  	long number = 580787; //ID number from last record of DrugsDictionaryCompilerLoop2.java + 1

	    for(String eachDrug : pharmGKBList) {
	    	//generate unique drugID
			number = number+1;
			drugID = "CD".concat(String.format("%08d", number));
			
			pharmGKBID = eachDrug.substring(0, eachDrug.indexOf("###"));
			pharmGKBSynonyms = eachDrug.substring(eachDrug.indexOf("###")+3);
			
	    	//write synonyms to file
			bw.append(drugID+"\t"+umlsSynonyms+"\t"+drugBankSynonyms+"\t"+pharmGKBSynonyms+"\t"+cuiTui+"\t"+drugBankID+"\t"+pharmGKBID);
			bw.append("\n");
		}

	    br.close();
	    bw.close();

	    long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Execution time in milliseconds: " + elapsedTime);
	}

}
