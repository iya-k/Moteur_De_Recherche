package utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public abstract class Commons 
{
	String PATH = "./resources/frwiki-debut.xml";//fichier source
	String OUT_XML_FILE = "./resources/corpus.xml";//fichier destination
	String CATEGORY = "sport";//category pour le filtrer
	String DICO_FILE = "./resources/dico.txt";//dictionnaire
	String STOP_WORDS = "./resources/stopWords.txt";//dictionnaire


	
	protected PrintWriter createFile(String path)
	{
		PrintWriter wr = null;
		try {
			wr = new PrintWriter(path);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		return  wr;
	}
	
	protected void stringToWrite(PrintWriter writer, String toSave)
	{
		writer.println(toSave);
		writer.flush();

	}
	
	public void closeFile(PrintWriter writer)
	{
		writer.close();
	}
	
}
