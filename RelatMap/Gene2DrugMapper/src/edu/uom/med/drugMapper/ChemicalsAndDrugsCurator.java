package edu.uom.med.drugMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Program filters a list of chemical and drugs names from the customized chemicals and drugs dictionary. 
 * This is required for the drug mapping using MedTagger
 * 
 * @author Kalpana Raja
 *
 */

public class ChemicalsAndDrugsCurator {
	
	//global variable declaration
	static ArrayList<String> chemicalsList = new ArrayList<String>();
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		String line="";
		
		try {
			FileReader fr = new FileReader("resources/lookup/ChemicalsAndDrugsLexicon");
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter("resources/lookup/ChemicalsAndDrugsList.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			
			while((line = br.readLine())!=null) {
					String[] arrLine = line.split("\\|");
					if(arrLine[1].contains(",")) {
						arrLine[1] = arrLine[1].replaceAll(",", "");
					}
					else if(arrLine[1].contains("-")) {
						arrLine[1] = arrLine[1].replaceAll("-", " ");
					}
					
					if(!chemicalsList.contains(arrLine[1])) {
						chemicalsList.add(arrLine[1].toLowerCase());
						System.out.println(arrLine[1].toLowerCase());
					}
			}
			
			for(String each : chemicalsList) {
				bw.append(each);
				bw.append("\n");
			}
			
			System.out.println("Size: "+chemicalsList.size());
			
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}	
	}
	
}
