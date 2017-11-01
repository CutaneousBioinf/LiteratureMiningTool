package edu.uom.med.geneDiseaseMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/** 
 * The program assigns diseases annotated with each PMID. We use MeSH index for retrieving PMIDs (in a previous
 * step) for a specific disease and use the same index for assigning the disease annotations to each PMID.  
 * 
 *  
 * @author Kalpana Raja
 *
 */

public class PubMedDiseaseMapper {
	
	/**
	 * Program execution starts here
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String arg1 = args[0]; //INPUT_FOLDER -- folder_containing_list_of_files
		String arg2 = args[1]; //OUTPUT_FILE -- pubmed_disease_mapping_43diseasesUnderStudy
		String arg3 = args[2]; //INPUT_FILE -- files_inside_input_folder

		String line="", disease="", fileName="";
		int count=0;
		
		//get the list of file names related diseases under study
		ArrayList<String> traitsFiles43	= getFileNames();
		
		File[] files = new File(arg1).listFiles();
			 
		try {
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
		
			for (File file : files) {
				if (file.isFile()) {
					fileName = file.getName();
					
					//perform only for 43 diseases
					if(!traitsFiles43.contains(fileName)) continue;
					
					if(fileName.contains("-")) {
						disease = fileName.toLowerCase().replaceAll("-", " ").replaceAll(".txt", "");
					}
					else {
						disease = fileName.toLowerCase().replaceAll(".txt", "");
					}
					
					FileReader fr = new FileReader(arg3+fileName);
					BufferedReader br = new BufferedReader(fr);
					while((line=br.readLine()) != null) {
						if(line.contains("<Id>")) {
							line = line.replaceAll("<Id>", "");
							line = line.replaceAll("</Id>", "");
						}
						
						bw.append(line+"\t"+disease);
						bw.append("\n");
						
						count++;
						
						//if(count==500) break;
					}
					System.out.println("Total number of PubMed: "+count);
					br.close();
					br.close();
				}
			}
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Execution time in milliseconds: "+elapsedTime);
	}
	
	/**
	 * Method to get list of file names related to 43 complex diseases
	 * 
	 * @return
	 */
	
	public static ArrayList<String> getFileNames() {
		ArrayList<String> traitsFiles43 = new ArrayList<String>();
		
		traitsFiles43.add(FILE1_NAME);
		traitsFiles43.add(FILE2_NAME);
		traitsFiles43.add(FILE3_NAME);
		traitsFiles43.add(FILE4_NAME);
		traitsFiles43.add(FILE5_NAME);
		//continue for all files
		
		return traitsFiles43;
	}
}
