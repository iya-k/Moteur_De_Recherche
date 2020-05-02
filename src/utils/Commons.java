package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import tp1.CLI;
import tp1.Page;


/*
 * 
Chargement...
javax.xml.stream.XMLStreamException: ParseError at [row,col]:[120269336,401]
Message: JAXP00010004: The accumulated size of entities is "50,000,001" that exceeded the "50,000,000" limit set by "FEATURE_SECURE_PROCESSING".
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl.next(XMLStreamReaderImpl.java:652)
	at java.xml/com.sun.xml.internal.stream.XMLEventReaderImpl.nextEvent(XMLEventReaderImpl.java:83)
	at ProcessXMLNew.loadDocument(ProcessXMLNew.java:111)
	at ProcessXMLNew.traitement(ProcessXMLNew.java:55)
	at ProcessXMLNew.main(ProcessXMLNew.java:215)
Fin du chargement
171843
Fini en: 110.203043297 secondes.
RAM : 1047950944 octets

 * */

public abstract class Commons extends Utils_Files
{
	public static final double EPSILON = (double) 1.0/1000.0;
	public static final float ZAP_D = (float) 0.15;
	public static final int TAILLE_DICO = 20000;
	public static final int SEUIL_MOT = 20;
	public static final String PATH = "./resources/frwiki-debut.xml";//fichier source
	public static final String CORPUS_XML_FILE = "./resources/corpus.xml";//fichier destination
	//public static final String PATH_2 = "/Users/mmekaba/home/University/M2/Semestre_2/MAAIN/TP/Corpus/corpus.xml";
	public static final String DICO_FILE = "./resources/dico.txt";//dictionnaire
	public static final String STOP_WORDS = "./resources/stopWords.txt";//dictionnaire 
	//public static final String OUT_TXT_FILE ="./resources/sortie.txt";

	private ArrayList<String> links;
	protected static ArrayList<Page> visited_pages = new ArrayList<Page>();
	//protected static HashMap<HashMap<Integer, Integer>, Integer> mots_pages;
	protected static HashMap<Integer, HashMap<Integer, Integer>> mots_pages;
	protected static HashMap<Integer, ArrayList<String>> liensExternes;
	//indice du mot dans le dico, liste des id et occurences du mot dans la page correspondant à l'id
	

	/**
	 * @return the links
	 */
	protected ArrayList<String> getLinks() {
		return links;
	}

	protected void setLinks(ArrayList<String> links) {
		this.links = links;
	}
	void addVisited(Page page)
	{
		visited_pages.add(page);
	}
	
	
	//compte le nombre de mots dans un text tout en le normalisant
	protected Page stripAccentsAndCount(String text, Page page)
	{
		text = stripAccents(text);
		//Map<String, Integer> wordOccurence = new HashMap<String, Integer>();
		String[] words;
		
		words = Arrays.asList(text.replaceAll("[^a-zA-Z]", " ")
				.split(" "))
				.stream().toArray(String[]::new);
		//System.out.println(page.getTitle()+"\n\n ensemble de mots------"+text );
		
		for(String mot: words)
		{
			getWordOccurence(page, mot, 1);
			//int nbOccur = getWordOccurence(page, mot, 1);

			//System.out.println("\n\n ------"+mot);
//			Word w = existWord(mot);
//			if(w != null)//si le mot est déjà dans la liste, on recupere le nbre d'occurence dans cette page
//			{
//				listWords.remove(listWords.indexOf(w));
//				w.setNbrOccurence(w.getNbrOccurence() + nbOccur);
//				w.getPages().add(page);
//				
//				
//				//System.out.println("\n\n if "+w.getWord()+" => "+w.getNbrOccurence());
//			}
//			else//sinon, on cree le mot avec les infos essentiels
//			{
//				w = new Word(page, mot, nbOccur);
//				w.setNbrOccurence(w.getNbrOccurence() + nbOccur);
//				//System.out.println("\n\n else "+w.getWord()+" => "+w.getNbrOccurence());
//				
//				
//				//System.out.println("\n\n ------"+mot+"---------------- => "+w.getNbrOccurence());
//			}
//			listWords.add(w);
		}

		return page;
	}
	
	//ne sert à rien, doit être supprimé
	protected List<String> withOutStopWords(String[] words)
	{
		List<String> newText = new ArrayList<>();
		
		for(int i = 0; i < words.length; i++)
		{
			//if(!stopWords().contains(words[i]))
			//{
				newText.add(words[i]);
				//System.out.println(words[i]);
			//}
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
//	protected Word existWord(String word)
//	{
//		for(Word w: listWords)
//		{
//			if(w.getWord().equals(word))
//			{
//				return w;
//			}
//		}
//		return null;
//	}
	
	public boolean isWordInPage(String word, Page p) 
	{
		
		if(p.getOccur_Mots().containsKey(word))
		{
			return true;
		}
		return false;
	}
	
	/*
    Fonctions de création d'une matrice à partir d'un fichier
  */

 public static int matrice_size(String matrice_file) {
	 int size = -1;
	 try {
		 BufferedReader br = new BufferedReader(new FileReader(matrice_file));
		 String line;
		 while ((line = br.readLine()) != null) {
			 String[] split = line.split(" ");
			 Integer x = Integer.parseInt(split[0]);
			 Integer y = Integer.parseInt(split[1]);
			 if (x > size) {
				 size = x;
			 }
			 if (y > size) {
				 size = y;
			 }
		 }

		 br.close();
	 } catch (IOException e) {
		 System.out.println("'" + matrice_file + "' doesn't exist!");
	 }

	 return size;
 }

 public static void push_matrice(CLI cli, int origin_node, LinkedList<Integer> values) 
 {
	 int size = values.size();
	 if (size == 0) {
		 return;
	 }
	 float value = 1 / (float) size;
	 for (int i = 0; i < size; i++) {
		 cli.edit_value(origin_node, values.get(i), value);
	 }
 }

 public static CLI getMatriceFromFile(String filename) throws IOException {
	 CLI cli = new CLI(matrice_size(filename) + 1);
	 try {
		 BufferedReader br = new BufferedReader(new FileReader(filename));
		 String line;
		 int pos = -1;
		 LinkedList<Integer> values = new LinkedList<Integer>();
		 while ((line = br.readLine()) != null) 
		 {
			 if (line.charAt(0) == '#')
					continue;
			 String[] split = line.split("[\\s\\t]");
			 Integer x = Integer.parseInt(split[0]);
			 Integer y = Integer.parseInt(split[1]);     
			 if (pos == x) {
				 values.add(y);
			 } else {
				 push_matrice(cli, pos, values);
				 values = new LinkedList<Integer>();
				 values.add(y);
				 pos = x;
			 }
		 }
		 push_matrice(cli, pos, values);

		 br.close();
	 } catch (IOException e) {
		 throw new IOException(e);
	 }
	 return cli;
 }

 public static boolean test_file(String filename) 
 {
	 try {
		 getMatriceFromFile(filename);
	 } catch (IOException e) {
		 System.out.println("'" + filename + "' doesn't exist.");
		 return false;
	 }
	 return true;
 }


}
