package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class Commons 
{
	String PATH = "./resources/frwiki-debut.xml";//fichier source
	String OUT_XML_FILE = "./resources/corpus.xml";//fichier destination
	String CATEGORY = "sport";//category pour le filtrer
	String DICO_FILE = "./resources/dico.txt";//dictionnaire
	String STOP_WORDS = "./resources/stopWords.txt";//dictionnaire
	BufferedReader buffer_reader ;
	InputStream isR ;
	InputStreamReader isrR;


	
	protected PrintWriter createFile(String path)
	{
		PrintWriter wr = null;
		try {
			wr = new PrintWriter(path);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		System.out.println("fichier creer "+path);
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
	
	protected BufferedReader readFile(String path)
	{	
		try {
			isR = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			System.out.println("Erreur isR = new FileInputStream(filename) ");
			e.printStackTrace();
		}

		isrR = new InputStreamReader(isR);
		buffer_reader = new BufferedReader(isrR);
		return  buffer_reader;
	}
}
