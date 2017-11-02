package edu.uom.med.localPubmedDatabaseCompiler;

import java.io.IOException;

/**
 * PubMed database consists of >28 million biomedical articles. It is the largest repository available freely.
 * Text Mining and Natural Language Processing (NLP) researches use the entire database in order to retrieve
 * the biomedical information of interest. However, it is impossible to access the entire database online and it is 
 * prohibited. PubMed provided all the published articles through ftp for mining researches. Thus the entire database 
 * can be downloaded and saved locally. We present a Java Project that can compress the downloaded articles into a
 * binary file. The project consists of two tasks that can be used in combination or separately from the wrapper class:
 * LocalPubmedDatabaseCompiler.java. The two tasks are: (1) decompressing the zip files downloaded from PubMed ftp and
 * (2) compressing and indexing of PubMed articles.
 * 
 * 
 * @author Kalpana Raja and Troy Cao
 *
 */

public class LocalPubmedDatabaseCompiler {
	
	/**
	 * The program execution starts from here
	 * 
	 * @param args
	 * @throws IOException
	 */
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		//decompressing the zip files downloaded from PubMed ftp site
		String filesInputFolderName = "/Use/your/folder/with/downloaded/zip_files/";
		String filesOutputFolderName = "/declare/folder/for/decomposed/files/"; 
		Unzipper unzipper = new Unzipper();
		unzipper.fileUnzipper(filesInputFolderName, filesOutputFolderName);
		
		//Compressing and indexing of PubMed articles
		String inputFolderName = "/declare/folder/for/decomposed/files/";
		String outputFolderName = "/Use/your/folder/for/local/pubmed/database";
		XMLProcessorAndIndexer xmlProcessorAndIndexer = new XMLProcessorAndIndexer();
		xmlProcessorAndIndexer.processorAndIndexer(inputFolderName, outputFolderName);
		
		long endTime = System.currentTimeMillis();
		System.out.println("Execution completed in " + (endTime - startTime) + " milliseconds");
		
	}

}
