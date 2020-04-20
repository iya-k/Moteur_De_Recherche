package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public abstract class Commons 
{
	public static final double epsilon = (double) 1.0/1000.0;
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
	
	protected void writeStartElement(XMLStreamWriter out, String toSave)
	{
		try {
			out.writeCharacters("\n");
			out.writeStartElement(toSave);
			out.flush();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

	}
	protected void writeEndElement(XMLStreamWriter out)
	{
		try {
			out.writeEndElement();
			out.flush();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

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
	protected ArrayList<String> stripAccents(ArrayList<String> dico)
	{
		
		ArrayList<String> words = new ArrayList<String>();
		for(String s: dico)
		{
			String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
			Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");//supprime les accents
			String t = (pattern.matcher(temp).replaceAll("")).toLowerCase();//transforme en miniscule
			
			
			if(t.equals(s) && !words.contains(t))
			{
				//System.out.println(s+"_____"+t);
				words.add(t);
			}
			else if(!words.contains(t))
			{
				words.add(t);
				//System.out.println("else _____"+s+"_____"+t);
			}
		}
		/*
		int i = 0;
		while(i < dico.size() )
		{
			System.out.println(dico.get(i));
			i++;
		}
		/*
		 * String retour = pattern.matcher(temp).replaceAll("");
		 * retour.replaceAll("[^A-Za-z]", " ");
		 * return retour.replaceAll("[^A-Za-z]", " ").toLowerCase();//transforme en miniscule
		 */
		return words;
	}
	protected int stripAccentsAndCount(String text, String mot)
	{
		int occur = 0;
		String[] words;
		words = Arrays.asList(text.replaceAll("[^a-zA-Zçéèàêôîûöüïäù]", " ")
				.split(" "))
				.stream().filter(x -> x.length()>1).toArray(String[]::new);
		for(String word: words)
		 {
			String temp = Normalizer.normalize(word, Normalizer.Form.NFD);
			Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");//supprime les accents
			String t = (pattern.matcher(temp).replaceAll("")).toLowerCase();//transforme en miniscule
			System.out.println(word);
			if(t.equals(mot))
			{
				occur ++;
			}
		 }
		return occur;
	}
	
	public static final int nbreOccur(String textTofind, String regex) 
	{
		Matcher matcher = Pattern.compile(regex).matcher(textTofind);
		int occur = 0;
		while(matcher.find()) 
		{
			occur ++;
		}
		return occur;
	}
	
	protected void stopWords() 
	{
		ArrayList<String> words = new ArrayList<String>();
		
	}
}
