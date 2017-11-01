Project Title: Gene2DrugMapper


Introduction: Gene2DrugMapper project is meant to map genes (gene name / symbol / aliases), and chemicals/drug (names and synonyms) in a sentence from PubMed abstract. It also filters the sentences mapped with a gene and a chemical/drug.  


Prerequisites: 
** Chemical/drug lexicon is required to run the project. The lexicon can be compiled from three resources namely UMLS Metathesaurus, DrugBank and PharmGKB (refer DrugDict:DrugsDictionaryCompiler and ConceptMap:UMLSMetathesaurusCompiler for related Java codes).
** We use gene2pubmed resource from NCBI for mapping genes. Please refer to gene2pubmedMapper package within Gene2DrugMapper project for processing the resource.  
** The project also requires a local version of PubMed database (refer ConceptMap:LocalPubmedDatabaseCompiler and ConceptMap:PubMedXMLParser for related Java codes). 
** MedTagger is required for executing the code and it has to be merged with our Java code (e.g. src/edu/mayp/nlp/). MedTagger is meant for indexing biomedical concepts (e.g. disease, chemical, drug) based on a lexicon and distributed as an Open health Natural Language Processing project (OHNLP). 
** The project requires a range of existing Java libraries: apache-logging-log4j.jar, commons-lang-2.3.jar, commons-logging-1.1.1.jar, org.apache.commons.io.jar, Stanford lexical parser and Apache Lucene (version 5.1.0 or above). You can download them from the respective contributor's URL and add to the project's build path.  
  
---- RUN IN AN IDE ----

The entire project can be pulled into Java IDE, such as eclipse. You can compile the file Disease2DrugMapper/edu/uom/med/geneDrugMapper/GeneDrugMapper.java.


---- COMPILE AND RUN ON THE COMMAND LINE ----

Mapping of genes and chemicals/drugs can be achieved with the following commands: 
						
						javac GeneDrugMapper.java 
						java GeneDrugMapper INPUT_FILE OUTPUT_FILE 

Alternatively, you can generate a jar file of the entire project (GeneDrugMapper.jar, 
say for example)and execute the jar file from the command line using the standard command. 

						java -jar GeneDrugMapper.jar INPUT_FILE OUTPUT_FILE


Input: The input is one or more sentences from PubMed abstracts assigned with PMID (see data/sample_input). 

Output: The output displays input sentences with the mapped genes (name / synonym / symbol), chemical/drug, gene symbol and chemical/drug ID from the chemical/drug lexicon (see data/sample_output).


Java version used for development: JavaSE-1.8

Authors: Kalpana Raja 
Affiliation: Department of Dermatology, University of Michigan, Ann arbor 48019, MI, USA

