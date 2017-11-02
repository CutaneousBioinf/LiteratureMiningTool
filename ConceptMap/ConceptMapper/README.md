Project Title: ConceptMapper


Introduction: The project is meant for mapping concepts such as chemicals/drugs, diseases and genes in a sentence.  

Prerequisites: The project requires gene2pubmed resource from NCBI for gene mapping. It requires chemical/drug lexicon for chemicals/drugs mapping. Please refer to DrugDict project for generating one. It requires disease lexicon for disease mapping. Please refer to RelatMap:DiseaseDiseaseMapper project for generating one. 


---- RUN IN AN IDE ----

The entire project should be pulled into Java IDE, such as eclipse. The execution starts with the respective java file (Please see below for details). 


---- COMPILE AND RUN ON THE COMMAND LINE ----

The following commands are used for mapping chemicals/drugs: 
						
				javac ChemicalsAndDrugsMapper.java
				java ChemicalsAndDrugsMapper INPUT_FILE OUTPUT_FILE 

The following commands are used for mapping disease:

				javac DiseaseMapper.java
				java DiseaseMapper INPUT_FILE OUTPUT_FILE

The following commands are used for mapping genes:

				javac Gene2PubMedMapperExactAndNormalizedMatch.java
				java Gene2PubMedMapperExactAndNormalizedMatch INPUT_FILE1 INPUT_FILE2 OUTPUT_FILE1 OUTPUT_FILE2


Input: See data folder for sample input file for each concept mapping.

Output: See data folder for sample output file for each concept mapping.


Java version used for development: JavaSE-1.8

Authors: Kalpana Raja PhD

Affiliation: Department of Dermatology, University of Michigan, Ann arbor 48019, MI, USA
