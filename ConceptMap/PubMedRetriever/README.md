Project Title: LocalPubmedDatabaseCompiler


Introduction: The project creates a local version of PubMed database in a binary format using Apache Lucene. 

Prerequisites: PubMed Database should be downloaded from its FTP site (ftp://ftp.ncbi.nlm.nih.gov/pubmed/baseline/) and stored locally. The downloaded files are .zip files. The project automatically unzips the input files prior to processing. Apache Lucene is required for compiling a local version of PubMed database.


---- RUN IN AN IDE ----

The entire project should be pulled into Java IDE, such as eclipse. Please make sure to update the location of input and output folders in the wrapper class: LocalPubmedDatabaseCompiler.java. 


---- COMPILE AND RUN ON THE COMMAND LINE ----

Please make sure to update the location of input and output folders in the wrapper class: LocalPubmedDatabaseCompiler.java. The following commands are used for executing the project: 
						
						javac LocalPubmedDatabaseCompiler.java
						java LocalPubmedDatabaseCompiler  

Alternatively, a jar file of the entire project (LocalPubmedDatabaseCompiler.jar, say for example) can be generated. The execution of the jar file is achieved with the following command.

						java -jar LocalPubmedDatabaseCompiler.jar


Input: PubMed database as zip files downloaded from the FTP site. 

Output: Compressed version of PubMed database inside the folder declared within the wrapper class. 

***************************************************************************************

Project Title: PubMedXMLParser


Introduction: For a list of PMIDs, the project retrieves PubMed abstracts from the local version of PubMed database using Apache Lucene. The processing consists of two steps: (i) abbreviation expansion, where the abbreviations are automatically identified and replaced with the original term, and (ii) sentence segmentation, where an abstract is split into individual sentences and each sentence is assigned with the respective PubMed ID.   

Prerequisites: Local version of PubMed database (refer our project LocalPubmedDatabaseCompiler) and Apache Lucene.  


---- RUN IN AN IDE ----

The entire project should be pulled into Java IDE, such as eclipse. 


---- COMPILE AND RUN ON THE COMMAND LINE ----

The following command is used for executing the project: 
						
						javac PubMedXMLParser.java
						java PubMedXMLParser INPUT_FILE OUTPUT_FILE  

Alternatively, a jar file of the entire project (LocalPubmedDatabaseCompiler.jar, say for example) can be generated. The following command is ued for executing the jar file. 

						java -jar PubMedXMLParser.jar INPUT_FILE OUTPUT_FILE


Input: List of PubMed IDs. (refer data/sample_input) 

Output: List of sentences assigned with respective PMIDs. (refer data/sample_output)


Java version used for development: JavaSE-1.8

Authors: Kalpana Raja and Troy Cao

Affiliation: Department of Dermatology, University of Michigan, Ann arbor 48019, MI, USA
