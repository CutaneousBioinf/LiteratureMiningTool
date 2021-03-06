package edu.uom.med.geneDrugAssociationGoldStandard;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Program replaces drug ID from resource with drug ID from Chemicals/Drugs lexicon. 
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class DrugnameToLexicondrugidMapper {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE_resource -- processed file from CTD / DrugBank / PharmGKB
		String arg2 = args[1]; //OUTPUT_FILE_resource

		String line="", drugID="";
		Hashtable<String, String> drugsLexicon = new Hashtable<String, String>();
		
		try {
			//lexicon
			FileInputStream fis0 = new FileInputStream("resources/lookup/ChemicalsAndDrugsLexicon");
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
				String[] arrLine = line.split("\\|");
				drugsLexicon.put(arrLine[1], arrLine[4]);
			}
		    System.out.println("Lexicon read is complete!!!");
			
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
			
			FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br0.readLine()) != null) {
				if(line.startsWith("Gene_symbol")) {
					bw.append(line+"\tDrug_ID_from_lexicon");
					bw.append("\n");
					continue;
				}
				
				String[] arrLine = line.split("\t");
				drugID = drugsLexicon.get(arrLine[1]);
				
				bw.append(line + "\t" + drugID);
				bw.append("\n");
			}
			
			br0.close();
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
}
