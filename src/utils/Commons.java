package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import tp1.Page;
import tp1.Word;

public abstract class Commons 
{
	public static final double epsilon = (double) 1.0/1000.0;
	public static final int TAILLE_DICO = 1000;
	public static final String PATH = "./resources/frwiki-debut.xml";//fichier source
	public static final String OUT_XML_FILE = "./resources/corpus.xml";//fichier destination
	public static final String[] CATEGORY =  {" sport", " tenis", " judo", " football"};//category pour le filtrer
	public static final String DICO_FILE = "./resources/dico.txt";//dictionnaire
	public static final String STOP_WORDS = "./resources/stopWords.txt";//dictionnaire 
	//public static final String OUT_TXT_FILE ="./resources/sortie.txt";

	private ArrayList<String> dico;
	private ArrayList<String> links;
	private ArrayList<Page> listPages = new ArrayList<Page>();
	protected ArrayList<Page> visited_pages;
	//private Page aPage;
	//private Word aWord;
	private ArrayList<Word> listWords = new ArrayList<Word>();
	private  BufferedReader buffer_reader ;
	private InputStream isR ;
	private InputStreamReader isrR;
	private PrintWriter writerDico;
	//protected static HashMap<HashMap<Integer, Integer>, Integer> mots_pages;
	protected static HashMap<Integer, HashMap<Integer, Integer>> mots_pages;
	protected static HashMap<Integer, ArrayList<String>> liensExternes;
	//indice du mot dans le dico, liste des id et occurences du mot dans la page correspondant à l'id
	
	/**
	 * @return the dico
	 */
	protected ArrayList<String> dictionary()
	{
		dico();
		return dico;
	}
	/**
	 * @param dico the dico to set
	 */
	protected void setDico(ArrayList<String> dico) {
		this.dico = dico;
	}

	/**
	 * @return the links
	 */
	protected ArrayList<String> getLinks() {
		return links;
	}

	/**
	 * @param links the links to set
	 */
	protected void setLinks(ArrayList<String> links) {
		this.links = links;
	}

	/**
	 * @return the listPages
	 */
	protected ArrayList<Page> getListPages() {
		return listPages;
	}

	/**
	 * @param listPages the listPages to set
	 */
	protected void setListPages(ArrayList<Page> listPages) {
		this.listPages = listPages;
	}

	/**
	 * @return the listWords
	 */
	protected ArrayList<Word> getlistWords() {
		return listWords;
	}

	/**
	 * @param listWords the listWords to set
	 */
	protected void setlistWords(ArrayList<Word> listWords) {
		this.listWords = listWords;
	}

	void addVisited(Page page)
	{
		visited_pages.add(page);
	}
	//creation d'un fichier
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
			out.writeCharacters("\n");
			out.writeEndElement();
			out.flush();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	//lire un fichier
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
	//normaliser un ensemble de mot
	/*
	protected String stripAccentsText(String text)
	{
		//System.out.println("-----------------titre avant : "+text);
		String result = "";
		String[] words;
		words = Arrays.asList(stripAccents(text).replaceAll("[^a-zA-Z ]", " ")
				.split(" ")).toArray(String[]::new);
		int taille = 0;

		for(String word: words)
		{
			result += word;
			taille ++;
			if(taille < words.length)
			{
				result += " ";
			}
		}
		//System.out.println("-----------------titre: "+result);
		return result;
	}
	*/
	
	//compte le nombre de mots dans un text tout en le normalisant
	protected Page stripAccentsAndCount(String text, Page page)
	{
		text = stripAccents(text);
		//Map<String, Integer> wordOccurence = new HashMap<String, Integer>();
		String[] words;
		
		words = Arrays.asList(text.replaceAll("[^a-zA-Z]", " ")
				.split(" "))
				.stream().toArray(String[]::new);
		words = withOutStopWords(words).toArray(String[]::new);
		//System.out.println(page.getTitle()+"\n\n ensemble de mots------"+text );
		
		for(String mot: words)
		{
			int nbOccur = getWordOccurence(page, mot, 1);
			//page.setOccur_Mots(page.getOccur_Mots());

			//System.out.println("\n\n ------"+mot);
			Word w = existWord(mot);
			if(w != null)//si le mot est déjà dans la liste, on recupere le nbre d'occurence dans cette page
			{
				listWords.remove(listWords.indexOf(w));
				w.setNbrOccurence(w.getNbrOccurence() + nbOccur);
				w.getPages().add(page);
				
				
				//System.out.println("\n\n if "+w.getWord()+" => "+w.getNbrOccurence());
			}
			else//sinon, on cree le mot avec les infos essentiels
			{
				w = new Word(page, mot, nbOccur);
				w.setNbrOccurence(w.getNbrOccurence() + nbOccur);
				//System.out.println("\n\n else "+w.getWord()+" => "+w.getNbrOccurence());
				
				
				//System.out.println("\n\n ------"+mot+"---------------- => "+w.getNbrOccurence());
			}
			listWords.add(w);
		}

		return page;
	}
	
	protected List<String> withOutStopWords(String[] words)
	{
		List<String> newText = new ArrayList<>();
		
		for(int i = 0; i < words.length; i++)
		{
			if(!stopWords().contains(words[i]))
			{
				newText.add(words[i]);
			}
		}

		return newText;
	}
	//normaliser les mots
	protected String normalize(String word)
	{
		String temp = Normalizer.normalize(word, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");//supprime les accents
		String t = (pattern.matcher(temp).replaceAll(""));
		//System.out.println(t);
		return t;
	}

	/**
	 * generation de dictionnaire
	 */
	protected void dico()
	{
		writerDico = createFile(DICO_FILE);
		dico = new ArrayList<String> ();
		Map<String, Integer> occurences = new HashMap<String, Integer>();
		Collections.sort(listWords);
		
		//System.out.println("\n\n"+listWords.size());
		
		
		for(Word w: listWords)
		{
			//occurences.put(w.getWord(), w.getNbrOccurence());
			
			System.out.println(w.getWord()+": "+ w.getNbrOccurence());
		}
		
		occurences = occurences.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
				Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		/*
		 * 
		for(String s : occurences.keySet())
		{
			System.out.println(s + " ==> s");
		}
		 */
		String words[] = Arrays.copyOfRange(occurences.keySet().toArray(new String[occurences.size()]),
				occurences.keySet().size()-TAILLE_DICO, occurences.keySet().size());
		
		dico.addAll(Arrays.asList(words));
		//dico.removeAll(stopWords());
		
		//4. Trier cette liste par ordre alphabétique.
		Collections.sort(dico);
		int i = 0;
		while(i < dico.size() )
		{
			stringToWrite(writerDico, dico.get(i));
			i++;
		}
		
		System.out.println("\n\n ==============> dico---------"+ dico);
		
		writerDico.close();
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
	//recuperer le nombre d'occurences d'un mot dans un texte
	protected int getWordOccurence(Page p, String chaine, Integer nbr)
	{
		if(p.getOccur_Mots().containsKey(chaine))
		{
			p.getOccur_Mots().replace(chaine, p.getOccur_Mots().get(chaine)+nbr);
			
		}
		else
		{
			p.getOccur_Mots().put(chaine, nbr);
		}

		p.setOccur_Mots(p.getOccur_Mots());
		
		return p.getOccur_Mots().get(chaine);
	}
	//test si le mot existe déjà dans la liste
	protected Word existWord(String word)
	{
		for(Word w: listWords)
		{
			if(w.getWord().equals(word))
			{
				return w;
			}
		}
		return null;
	}
	public int wordOccurInPage(String word, Page p) 
	{
		int toReturn = 0;
		
		if(p.getOccur_Mots().containsKey(word))
		{
			toReturn = p.getOccur_Mots().get(word);
		}
		return toReturn;
	}
	
	public boolean isWordInPage(String word, Page p) 
	{
		
		if(p.getOccur_Mots().containsKey(word))
		{
			return true;
		}
		return false;
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
