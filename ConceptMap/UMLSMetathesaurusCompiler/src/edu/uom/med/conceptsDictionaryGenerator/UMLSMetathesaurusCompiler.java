package edu.uom.med.conceptsDictionaryGenerator;

import java.io.IOException;

/**
 * UMLS Metathesaurus is a large biomedical thesaurus consisting of concepts and 
 * synonyms from nearly 200 different vocabularies. The Metathesaurus also defines useful 
 * relationships between concepts. UMLS Metathesaurus can be downloaded from UMLS Terminology 
 * Services ((https://uts.nlm.nih.gov/home.html). A license is required for using the resource and 
 * can be requested at (https://www.nlm.nih.gov/research/umls/knowledge_sources/metathesaurus/).
 * The project is developed to process UMLS Metathesaurus in order to generate a dictionary of  
 * concepts that are in English language. Each concept or its synonyms is defined with a concept
 * name and an identifier, that consists of two parts: unique concept identifier (CUI) and unique
 * semantic type (TUI). While CUI identifies a concept or its synonyms, TUI defines whether a 
 * concept is a drug / disease / anatomy / physiology / other categories from UMLS MetaMap.
 * 
 * @authors Kalpana Raja and Troy Cao
 *
 */

public class UMLSMetathesaurusCompiler {
	
	/**
	 * The program execution starts from here
	 * 
	 * @param args
	 * @throws IOException
	 */
	
	public static void main(String[] args) throws IOException {
		
		//Execution time in milliseconds
		long startTime = System.currentTimeMillis();
		
		//Arguments
		String mrconsoFile = args[0]; //path for MRCONSO.RRF file within UMLS Metathesaurus
		String mrstyFile = args[1]; //path for MRSTY.RRF file within UMLS Metathesaurus
				
		//Retrieving concepts and synonyms from UMLS Metathesaurus
		MRCONSOPreferredVocabularyGenerator mrconsoPreferredVocabularyGenerator = 
				new MRCONSOPreferredVocabularyGenerator();
		mrconsoPreferredVocabularyGenerator.conceptsAndSynonymsExtractor(mrconsoFile);
		
		//Retrieving CUI and TUI association from UMLS MetaMap (Source is provided within resources folder)
		MRSTYCuiTuiGenerator mrstyCuiTuiGenerator = new MRSTYCuiTuiGenerator();
		mrstyCuiTuiGenerator.cuiTuiGenerator(mrstyFile);
		
		//Defining unique identifier for each concept or its synonyms as CUI_TUI
		MRSTYCuiTuiGrouper mrstyCuiTuiGrouper = new MRSTYCuiTuiGrouper();
		mrstyCuiTuiGrouper.cuiTuiGrouper();
		
		//Generating the dictionary of concepts from UMLS Metathesaurus and UMLS MetaMap
		MRCONSODictionaryPreparer mrconsoDictionaryPrepaer = new MRCONSODictionaryPreparer();
		mrconsoDictionaryPrepaer.conceptsDictionaryPreparer();
		
		//Displaying execution time in milliseconds
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Execution time in milliseconds: " + elapsedTime);
	}

}
