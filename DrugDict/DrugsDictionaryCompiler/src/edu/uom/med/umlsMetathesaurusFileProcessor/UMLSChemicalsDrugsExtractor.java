package edu.uom.med.umlsMetathesaurusFileProcessor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Program to retrieve Chemicals/Drugs from UMLS Metathesaurus by using the semantic types (TUI) related to chemicals /
 * drugs. The input file is the output from UMLSMetathesaurusCompiler project.
 * 
 * 
 * @author Kalpana Raja
 *
 */

public class UMLSChemicalsDrugsExtractor {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FILE -- UMLS Metathesaurus processed file
		String arg2 = args[1]; //OUTPUT_FILE_umls -- Chemicals/Drugs and synonyms from UMLS Metathesaurus
		
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
				if(!line.contains("T116") && 		//Amino Acid, Peptide, or Protein
						!line.contains("T195") &&	//Antibiotic
						!line.contains("T123") &&	//Biologically Active Substance
						!line.contains("T122") &&	//Biomedical or Dental Material
						!line.contains("T118") && 	//Carbohydrate
						!line.contains("T103") &&	//Chemical
						!line.contains("T120") &&	//Chemical Viewed Functionally 
						!line.contains("T104") &&	//Chemical Viewed Structurally
						!line.contains("T200") && 	//Clinical Drug
						!line.contains("T111") &&	//Eicosanoid
						!line.contains("T196") && 	//Element, Ion, or Isotope
						!line.contains("T126") &&	//Enzyme
						!line.contains("T131") && 	//Hazardous or Poisonous Substance
						!line.contains("T125") &&	//Hormone
						!line.contains("T129") && 	//Immunologic Factor
						!line.contains("T130") &&	//Indicator, Reagent, or Diagnostic Aid
						!line.contains("T197") && 	//Inorganic Chemical
						!line.contains("T119") &&	//Lipid
						!line.contains("T124") && 	//Neuroreactive Substance or Biogenic Amine
						!line.contains("T114") &&	//Nucleic Acid, Nucleoside, or Nucleotide
						!line.contains("T109") && 	//Organic Chemical
						!line.contains("T115") &&	//Organophosphorus Compound
						!line.contains("T121") && 	//Pharmacologic Substance
						!line.contains("T192") &&	//Receptor
						!line.contains("T110") && 	//Steroid
						!line.contains("T127")) 	//Vitamin
					{ continue; }
				
				bw.append(line);
				bw.append("\n");
				
				count++;
				//if(count==100) break;
				if(count%50000==0) System.out.println(count);
			}
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Execution time in milliseconds: "+elapsedTime);
	    System.out.println("Number of Chemicals and Drugs Concepts: "+count);
	}
	
}
