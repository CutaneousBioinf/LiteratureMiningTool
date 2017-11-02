package edu.uom.med.localPubmedDatabaseCompiler;


import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

/**
 * The Unzipper program implements an application that decomposses PubMed files in .xml.zip 
 * or .xml.gz formats. 
 * 
 * @author Kalpana Raja and Troy Cao
 *
 */

public class Unzipper {
	
	XMLProcessorAndIndexer xmlProcessorAndIndexer = new XMLProcessorAndIndexer();
	
	/**
	 * Method to collect all zip files
	 * 
	 * @param filesInputFolderName, filesOutputFolderName 
	 * @return
	 */
	public void fileUnzipper(String filesInputFolderName, String filesOutputFolderName) {
		
		System.out.println("Decompossing zip files....");
		
		byte[] buffer = new byte[1024];
		
		String zipFile = "", decomposedFile = "";
		int count=0;
		
		File folder = new File(filesInputFolderName);
		File[] listOfFiles = folder.listFiles();
		
		try{	
			//iterating through all zip files
			for (File file : listOfFiles) {
			    if (file.isFile()) {
			    	zipFile = file.getName();
			    	    	
			    	FileInputStream in = new FileInputStream(filesInputFolderName + zipFile);
			    	if(zipFile.endsWith(".gz")) {
			    		
			    		GZIPInputStream gzis = new GZIPInputStream(in); 
			    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    		
			    		int len;
			    		while ((len = gzis.read(buffer)) > 0) {
			    			baos.write(buffer, 0, len);
			    		}
			    		decomposedFile = baos.toString();
		            
			    		in.close();
			    		gzis.close();
			    	}
			    	else if(zipFile.endsWith(".zip")) {
			    		
			    		ZipInputStream zis = new ZipInputStream(in);
			    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    		
			    		int len;
			    		while ((len = zis.read(buffer)) > 0) {
			    			baos.write(buffer, 0, len);
			    		} 
			    		decomposedFile = baos.toString();
			    		
			    		in.close();
			    		zis.close();
			    	}
			    	
			    	//writing to file
			    	count++;
			    	FileOutputStream fos = new FileOutputStream(filesOutputFolderName+"File"+count+".txt");
				    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
				    BufferedWriter bw = new BufferedWriter(osr);
				    bw.append(decomposedFile);
				    bw.append("\n");
					bw.close(); 
			    }
			}
		}catch(IOException ex){
    		ex.printStackTrace(); 
    	}
	}
	
}


