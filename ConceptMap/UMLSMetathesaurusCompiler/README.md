Project Title: UMLSMetathesaurusCompiler


Introduction: The project is meant to process the concepts from UMLS Metathesaurus and generate a concept lexicon.


Prerequisites: UMLS Metathesaurus should be downloaded and installed in the local machine. Please refer to https://www.nlm.nih.gov/research/umls/knowledge_sources/metathesaurus/ for license requirements, download and installation.


---- RUN IN AN IDE ----

The entire project should be pulled into a Java IDE, such as Eclipse. 
The execution of the project starts from the wrapper class, UMLSMetathesaurusCompiler.java.


---- COMPILE AND RUN ON THE COMMAND LINE ----

The following commands are used to process UMLS Metathesaurus. 
						
		javac UMLSMetathesaurusCompiler.java
		java UMLSMetathesaurusCompiler MRCONSO_FILE_PATH MRSTYL_FILE_PATH 

Alternatively, a jar file of the entire project (UMLSMetathesaurusCompiler.jar, for example) can be generated. 
The following commands are used for executing the jar file:

		java -jar UMLSMetathesaurusCompiler.jar MRCONSO_FILE_PATH MRSTYL_FILE_PATH


Input: UMLS Metathesaurus downloaded and installed in the local system.

Output: The output files from the project are saved in the resources/secondaryDictionaries/ folder within 
the project.


Java version used for development: JavaSE-1.8

Authors: Kalpana Raja and Troy Cao

Affiliation: Department of Dermatology, University of Michigan, Ann Arbor 48109, MI, USA

