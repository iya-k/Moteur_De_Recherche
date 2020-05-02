package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import tp1.Page;

public abstract class Utils_Files 
{
	public static final double epsilon = (double) 1.0/1000.0;
	public static final int TAILLE_DICO = 10000;
	public static final String PATH = "frwiki-debut";//fichier source
	public static final String TXT = ".txt";//extension txt
	public static final String XML = ".xml";//extension xml
	public static final String CORPUS_XML_FILE = "corpus.xml";//fichier destination
	//public static final String PATH_2 = "/Users/mmekaba/home/University/M2/Semestre_2/MAAIN/TP/Corpus/corpus.xml";
	public static final String DICO_FILE = "dico";//dictionnaire
	public static final String STOP_WORDS = "stopWords";//Stopwords
	public static final String EMPTY_WORDS = "mots_vides";//mots vides 
	public static final String RESOURCES_DIR = "./resources/";//resources 
	//public static final String OUT_TXT_FILE ="sortie.txt";

	protected ArrayList<Page> visited_pages;
	private  BufferedReader buffer_reader ;
	private InputStream isR ;
	private InputStreamReader isrR;
	
	


		//lire un fichier
		protected BufferedReader readFile(String path)
		{	
			try {
				isR = new FileInputStream(getTxtFilename(path));
			} catch (FileNotFoundException e) {
				System.out.println("Erreur isR = new FileInputStream(filename) ");
				e.printStackTrace();
			}
			isrR = new InputStreamReader(isR);
			buffer_reader = new BufferedReader(isrR);

			return  buffer_reader;
		}

		//mots vides
		protected ArrayList<String> stopWords() 
		{
			BufferedReader buffer = readFile(STOP_WORDS);
			ArrayList<String> words = new ArrayList<String>();
			String word;
			try {
				while((word = buffer.readLine()) != null)
				{
					words.add(word);
				}

				buffer.close();

			} catch (IOException e) {
				System.out.println("Erreur buffer.readLine() ");
				e.printStackTrace();
			}
			/*
			String[] wordToRemove = new String[words.size()];
			int i = 0;
			while(i < words.size())
			{
				wordToRemove[i] = words.get(i);
				i++;
			}
			*/
			//return wordToRemove;
			return words;
		}
		
		public static List<String> getDataFromFile(String filename)
		{
			System.out.println("\n getDataFromFile "+filename);
			List<String> buf = new ArrayList<String>();	
			
			try (Scanner scan = new Scanner(new File(getTxtFilename(filename)))) 
			{
				while ( scan.hasNextLine()) 
				{
					String s = StringUtils.stripAccents(scan.nextLine());
					if(StringUtils.isNotBlank(s))
						buf.add(s);
				}
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("Fail to open the file, check his path !");
				e.printStackTrace();
			}
			return buf;
		}
	
		public static void writeToFile(Map<?,?> map, String path) throws IOException
		{
			System.out.println("\n writeToFile map ("+path+")");
			File file = new File (path);
			Writer writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream(file), "utf-8"));
			for(Object str : map.keySet()){
				writer.write((String)str + " : "+ map.get(str).toString()+"\n" );
				writer.flush();
			}
			writer.close();
		}
		
		public void writeToFile(List<String> words, String path) 
		{
			System.out.print("\n writeToFile words ");
			
			path = getTxtFilename (path);
			File file = new File (path);
			file.delete();
			try ( Writer writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream(file), "utf-8"))) {
				String prev = "";
				for (String el: words)
					if(StringUtils.isNotBlank(el) && !StringUtils.equalsIgnoreCase(el, prev))
					{
						writer.write(el+"\n");
						writer.flush();
						prev = el;
					}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		protected void stringToWrite(PrintWriter writer, String toSave)
		{
			writer.println(toSave);
			writer.flush();

		}
		
		public static String getTxtFilename(String path) 
		{
			System.out.println("("+RESOURCES_DIR+path+TXT+")");
			return RESOURCES_DIR+path+TXT ;
		}
		
		//supprimer les accents
		protected static String stripAccents(String s) {

			if(s.toLowerCase().contains("é"))
				s=s.toLowerCase().replace('é', 'e');
			if(s.toLowerCase().contains("è")) 
				s=s.toLowerCase().replace("è", "e");
			if(s.toLowerCase().contains("ê"))
				s=s.toLowerCase().replace("ê", "e");
			if(s.toLowerCase().contains("à")) 
				s=s.toLowerCase().replace("à", "a");
			if(s.toLowerCase().contains("â"))
				s=s.toLowerCase().replace("â", "a");
			if(s.toLowerCase().contains("ç")) 
				s=s.toLowerCase().replace("ç", "c");
			if(s.toLowerCase().contains("'"))
				s=s.toLowerCase().replace("'", " ");
			if(s.toLowerCase().contains("ù"))
				s=s.toLowerCase().replace("ù", "u");
			if(s.toLowerCase().contains("û"))
				s=s.toLowerCase().replace("û", "u");
			if(s.toLowerCase().contains("ô"))
				s=s.toLowerCase().replace("ô", "o");
			if(s.toLowerCase().contains("ö"))
				s=s.toLowerCase().replace("ö", "o");
			if(s.toLowerCase().contains("î"))
				s=s.toLowerCase().replace("î", "i");
			if(s.toLowerCase().contains("ï"))
				s=s.toLowerCase().replace("ï", "i");

			return s.toLowerCase();
		}
		

}
