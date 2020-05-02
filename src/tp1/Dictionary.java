package tp1;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import utils.Utils_Files;

public class Dictionary extends Utils_Files
{
	private ArrayList<String> dico;
	private HashMap<String, Integer> occurences;

	public Dictionary()
	{
		occurences = new HashMap<String, Integer>();
		dico = new ArrayList<>();
	}


	/**
	 * @return the dico
	 */

	public List<String> getDico()
	{
		BufferedReader buffer = readFile("sortie");

		String[] words = null;
		String ligne;

		try {
			while((ligne = buffer.readLine()) != null)
			{
				//System.out.println(ligne);
				words = Arrays.asList(stripAccents(ligne).replaceAll("[^a-zA-Z]", " ")
						.split(" "))
						.stream().filter(x -> x.length()>2).toArray(String[]::new);

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
		
		return sort();//trier le dictionnaire
	}

	private void generateHashMap(String chaine)
	{	
		if(StringUtils.isNotBlank(chaine))
		{
			//System.out.println(chaine);

			if(occurences.containsKey(chaine))
			{
				occurences.replace(chaine, occurences.get(chaine)+1);
			}
			else
			{
				occurences.put(chaine, 1);
			}
		}
	}
	/*
	 * 1. Dresser la liste des mots sur lesquels pourront porter les requêtes de l’utilisateur
	 * pourra par exemple prendre les 10000 mots les plus fréquents apparaissant dans le corpus,
	 *  dont tous ceux contenus dans le titre des pages.
	 */

	/**
	 * @return the occurences
	 */
	public HashMap<String, Integer> getOccurences() {
		return occurences;
	}

	private void createDico()
	{	
		//trier le map
		occurences = occurences.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
				Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		for(Entry<String, Integer> mapping : occurences.entrySet())
		{
			//System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
			//Supprimer les petits mots non discriminants comme le, la, un, de, sa, etc
			if(mapping.getKey().length() < 3)
			{
				//stringToWrite(writer, mapping.getKey().toLowerCase());
				occurences.remove(mapping.getKey());
			}
		}
		String words[] = Arrays.copyOfRange(occurences.keySet().toArray(new String[occurences.size()]),
				occurences.keySet().size() - TAILLE_DICO, occurences.keySet().size());

		dico.addAll(Arrays.asList(words));		

		//System.out.println(getDataFromFile(EMPTY_WORDS));

		sort();
	}


	//4. Trier cette liste par ordre alphabétique.
	public ArrayList<String> sort()
	{

		dico.removeAll(getDataFromFile(EMPTY_WORDS));//2. Supprimer de la liste les mots « vides » .

		ArrayList<String> sortedDico = new ArrayList<>();//3. Supprimer les accents, les majuscules et les redondances.

		int i = 0;
		for(String s: dico)
		{
			//System.out.println(getWordRoot(s));
			if(i > TAILLE_DICO)
			{
				break;
			}/*
			if(getWordRoot(s) == null)
			{
				continue;
			}
			 */
			if(!sortedDico.contains(s))
			{
				sortedDico.add(s);
			}
		}
		Collections.sort(sortedDico);

		System.out.println("taille "+sortedDico.size());
		writeToFile(sortedDico, DICO_FILE);
		return sortedDico;
	}
	//5. (Optionnel) Écrire une fonction permettant de trouver la « racine » des mots
	public String getWordRoot(String word) 
	{
		/*	
		Elements elt = Jsoup.connect("https://fr.wiktionary.org/wiki/"+word).get()
				.select("div#bandeau-voir > b > a#title");
				//.select("div#vtoolbar > ul > li#vitemselected > a > span");
		System.out.println(elt);

		 */
		FrenchStemmerImp fr = new FrenchStemmerImp();
		System.out.println(word+": "+fr.getStemmedWord(word));

		return null;
		//Jsoup.connect("https://fr.wiktionary.org/wiki/"+word);
		/*
		 * .connect("http://www.cnrtl.fr/definition/"+word).get()
				.select("div#vtoolbar > ul > li#vitemselected > a > span");
		 */
	}

	public static void main(String[] args) 
	{
		new Dictionary().getDico();
	}
}
