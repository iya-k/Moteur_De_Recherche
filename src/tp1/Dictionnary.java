package tp1;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import utils.Commons;

public class Dictionnary extends Commons
{
	BufferedReader buffer ;
	private ArrayList<String> dico;
	private  HashMap<String, Integer> occurences;
	static final int TAILLE_DICO = 100;
	static List<String> emptyWords;
	PrintWriter writer;
	String fileIn;
	String fileOut;
	static final String pathStopWord = "./resources/stopWords.txt";

	public Dictionnary(String fileIn,String fileOut)
	{
		occurences = new HashMap<String, Integer>();
		dico = new ArrayList<>();
		writer = super.createFile(pathStopWord);
		//emptyWords = new String["",""];
		this.fileIn = fileIn;
		this.fileOut = fileOut;
	}
	
	public static void main(String[] args) 
	{
		new Dictionnary("./resources/sortie.txt", "./resources/dico.txt").generateDictionnary();
	}
	
	public void generateDictionnary()
	{
		String ligne;
			
			buffer = super.readFile(fileIn);
			
			String[] words = null;
			
			try {
				while((ligne = buffer.readLine()) != null)
				{
					words = Arrays.asList(ligne.replaceAll("[^a-zA-Zçéèàêôîûöüïäù]", " ")
							.split(" "))
							.stream().filter(x -> x.length()>1).toArray(String[]::new);
					
					//System.out.println(sentence);
					 for(String word: words)
					 {
						 //System.out.println(word);
						 generateHashMap(word);
					 }
				}
			} catch (IOException e) {
				System.out.println("Erreur buffer.readLine() ");
				e.printStackTrace();
			}
			createDico();
			
			sort();//trier le dictionnaire
			
			try {
				buffer.close();
			} catch (IOException e) {
			}
	}
	private void generateHashMap(String chaine)
	{
		if(occurences.containsKey(chaine))
		{
			occurences.replace(chaine, occurences.get(chaine), occurences.get(chaine)+1);
		}
		else
		{
			occurences.put(chaine, 1);
		}

	}
	/*
	 * 1. Dresser la liste des mots sur lesquels pourront porter les requêtes de l’utilisateur
	 * pourra par exemple prendre les 10000 mots les plus fréquents apparaissant dans le corpus,
	 *  dont tous ceux contenus dans le titre des pages.
	 */
	
	private void createDico()
	{	
		//trier le map
		occurences = occurences.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
                				Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		
		for(Entry<String, Integer> mapping : occurences.entrySet())
		{
			if(mapping.getKey().length() < 3)
			{
				 super.stringToWrite(writer, mapping.getKey());
			}
			//System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
		}
        String words[] = Arrays.copyOfRange(occurences.keySet().toArray(new String[occurences.size()]),
        																occurences.keySet().size()-TAILLE_DICO, occurences.keySet().size());
        dico.addAll(Arrays.asList(words));
  /*      
		int i = 0;
		while(i < dico.size() )
		{
			System.out.println(dico.get(i));
			i++;
		}
*/
	}
	//2. Supprimer de la liste les mots « vides » (petits mots non discriminants) comme le, la, un, de, sa, etc. 
	protected void removedStopWords()
	{
		String word;
		buffer = super.readFile(pathStopWord);
		
		ArrayList<String> words = new ArrayList<String>();
		
		try {
			while((word = buffer.readLine()) != null)
			{
				 words.add(word);
			}
			dico.removeAll(words);
		} catch (IOException e) {
			System.out.println("Erreur buffer.readLine() ");
			e.printStackTrace();
		}
	}
	//3. Supprimer les accents, les majuscules et les redondances.
	protected ArrayList<String> stripAccents()
	{
		removedStopWords();
		
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
	//4. Trier cette liste par ordre alphabétique.
	public ArrayList<String> sort()
	{
		ArrayList<String> sortedDico = stripAccents();
		Collections.sort(sortedDico);
		int i = 0;
		while(i < sortedDico.size() )
		{
			System.out.println(sortedDico.get(i));
			i++;
		}
		return sortedDico;
	}
	//5. (Optionnel) Écrire une fonction permettant de trouver la « racine » des mots
	public String getRootElement(String word)
	{
		/*
		 * raciniser le texte d'un fichier du corpus.
		 * protected static ArrayList<String> stemming(File file) throws IOException {
		// RÃ©ponse complÃ¨te fournie
		ArrayList<String> words = (new FrenchStemmer()).normalize(file);		
		return words;
		
		return Jsoup
		    	.connect("http://www.cnrtl.fr/definition/"+word).get()
		    	.select("div#vtoolbar > ul > li#vitemselected > a > span")
		    	.first().text().replaceAll("[\\d]","").toLowerCase();
		    	
		    	 */
		return "";
	}
}
