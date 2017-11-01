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
 * step) for a specific disease and use the same index for assigning the disease annotations to each PMID. Our study   
 * includes 43 complex diseases and the program is retricted to these diseases only. For other diseases, the program
 * can be modified accordingly 
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

		String line="", disease="", fileName="";
		int count=0;
		
		//get the list of file names related 36 traits under study
		ArrayList<String> traitsFiles43	= getFileNames();
		
		File[] files = new File("/net/psoriasis/home/rkalpana/Projects/Complex_Traits_50/Disease_Drug_Mapping/Step_1_citation_retrieval/Abstract_MESH_Year/Output/").listFiles();
			 
		try {
			FileWriter fw = new FileWriter("pubmed_disease_mapping_43diseases.txt");
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
					
					FileReader fr = new FileReader("/net/psoriasis/home/rkalpana/Projects/Complex_Traits_50/Disease_Drug_Mapping/Step_1_citation_retrieval/Abstract_MESH_Year/Output/"+fileName);
					BufferedReader br = new BufferedReader(fr);
					while((line=br.readLine()) != null) {
						if(line.contains("<Id>")) {
							line = line.replaceAll("<Id>", "");
							line = line.replaceAll("</Id>", "");
						}
						bw.append(line+"\t"+disease);
						bw.append("\n");
						System.out.println(line+"\t"+disease);
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
		System.out.println("Execution time: "+elapsedTime);
	}
	
	/**
	 * Method to get list of file names related to 43 complex diseases
	 * 
	 * @return
	 */
	
	public static ArrayList<String> getFileNames() {
		ArrayList<String> traitsFiles43 = new ArrayList<String>();
		
		traitsFiles43.add("Psoriasis.txt");
		traitsFiles43.add("Psoriatic_arthritis.txt");
		traitsFiles43.add("Acne.txt");
		traitsFiles43.add("Age_related_macular_degeneration.txt");
		traitsFiles43.add("Ankylosing_spondylitis.txt");
		traitsFiles43.add("Alzheimers_disease.txt");
		traitsFiles43.add("Asthma.txt");
		traitsFiles43.add("Atopic_dermatitis.txt");
		traitsFiles43.add("Breast_cancer.txt");
		traitsFiles43.add("Bipolar.txt");
		traitsFiles43.add("Celiac_disease.txt");
		traitsFiles43.add("Chronic_lymphocytic_leukemia.txt");
		traitsFiles43.add("Chronic_obstructive_pulmonary_disease.txt");
		traitsFiles43.add("Hypercholesterolemia.txt");
		traitsFiles43.add("Crohns_disease.txt");
		traitsFiles43.add("Hepatitis.txt");
		traitsFiles43.add("Leprosy.txt");
		traitsFiles43.add("Lung_cancer.txt");
		traitsFiles43.add("Menarche.txt");
		traitsFiles43.add("Menopause.txt");
		traitsFiles43.add("Melanoma.txt");
		traitsFiles43.add("Motion_sickness.txt");
		traitsFiles43.add("Multiple_sclerosis.txt");
		traitsFiles43.add("Myopia.txt");
		traitsFiles43.add("Nasopharyngeal_carcinoma.txt");
		traitsFiles43.add("Obesity.txt");
		traitsFiles43.add("Ovarian_cancer.txt");
		traitsFiles43.add("Pancreatic_cancer.txt");
		traitsFiles43.add("Parkinsons_disease.txt");
		traitsFiles43.add("Primary_biliary_cirrhosis.txt");
		traitsFiles43.add("Prostate_cancer.txt");
		traitsFiles43.add("Rheumatoid_arthritis.txt");
		traitsFiles43.add("Sudden_cardiac_arrest.txt");
		traitsFiles43.add("Systemic_lupus_erythematosus.txt");
		traitsFiles43.add("Systemic_sclerosis.txt");
		traitsFiles43.add("Systolic_BP_increased.txt");
		traitsFiles43.add("Hypothyroidism.txt");
		traitsFiles43.add("Hyperthyroidism.txt");
		traitsFiles43.add("Hypertriglyceridemia.txt");
		traitsFiles43.add("Type_I_diabetes.txt");
		traitsFiles43.add("Type_II_diabetes.txt");
		traitsFiles43.add("Ulcerative_colitis.txt");
		traitsFiles43.add("Venous_thromboembolism.txt");
		
		return traitsFiles43;
	}
}
