Project Title: DrugsDictionaryCompiler


Introduction: DrugsDictionaryCompiler project is meant to compile a lexicon from UMLS Metathesaurus, DrugBank and PharmGKB. The lexicon contains list of drug concepts and their synonyms/aliases from all the resources. The format of the lexicon is designed to match with the format required by MedTagger, an open NLP resource from Mayo Clinic for extracting biomedical concepts. The output format is 'Normalized_drug_name | Drug_name || CHEM | Drug_ID'.     


Prerequisites: The resource files should be downloaded from UMLS Metathesaurus, DrugBank and PharmGKB.

UMLS Metathesaurus: Download and installation of UMLS Metathesaurus requires a license.Please refer https://www.nlm.nih.gov/research/umls/knowledge_sources/metathesaurus/. For processing the downloaded UMLS Metathesaurus, please refer to our project ConceptMap:UMLSMetathesaurusCompiler.

DrugBank: All drugs in the database is available to download as XML file at http://www.drugbank.ca/downloads. We provide a XMLParser to process the downloaded file (DrugNameSynonymExtractor.java).

PharmGKB: The resource drugs.zip can be downloaded from https://www.pharmgkb.org/downloads/. It contains 10 columns of information: PharmGKB_Accession_Id | Name | Generic_Name | Trade_Name | Brand_Mixtures | Type | Cross-reference | SMILES | Dosing_Guideline | External_Vocabulary. To create the dictionary, we consider the following 5 columns: PharmGKB_Accession_Id | Name | Generic_Name | Trade_Name | Cross-reference. DrugSynonymExtractor.java is developed to process the generic names and trade names, and to extract DrugBank_ID.
    
   
---- RUN IN AN IDE ----

The entire project should be pulled into Java IDE, such as eclipse. The execution of the project starts from main method within each package and documented clearly.


---- COMPILE AND RUN ON THE COMMAND LINE ----

Processing includes Java programs and Linux commands. Please refer Documentation/Compiling_chemicals_and_drugs_lexicon.docx.


Java version used for development: JavaSE-1.8

Author: Kalpana Raja

Affiliation: Department of Dermatology, University of Michigan, Ann arbor 48019, MI, USA

