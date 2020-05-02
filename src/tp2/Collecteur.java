package tp2;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import tp1.Page;
import tp1.Process;
import utils.Commons;

public class Collecteur extends Commons
{
	List<String> dico;	
	Process process;

	public Collecteur(List<String> list)
	{
		dico = list;
		process = new Process();
	}
	public void collect()
	{
		process.traitement();
		
		System.out.println("collect");

		mots_pages = new HashMap<>();
		for(String s: dico)
		{
			HashMap<Integer, Integer> occurByPage = new HashMap<Integer, Integer>();//id de la page, occurences du mot
			int indiceWord = dico.indexOf(s);
			
			for(Page p: visited_pages)
			{
				if(isWordInPage(s, p))
				{
					occurByPage.put(p.getId(), p.wordOccurence(s));
				}
			}
			mots_pages.put(indiceWord, occurByPage);
		}
		try {
			writeToFile(mots_pages, MOTS_PAGES);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		Map<String, Integer> mots = new HashMap<String, Integer>();
		for(Entry<Integer, Map<String, Integer>> mapping: words_of_a_page.entrySet())
		{
			System.out.println(mapping);
			System.out.println("\n");
			mots.putAll(mapping.getValue());
		}
		for(Entry<String, Integer> map: mots.entrySet())
		{

		}
		 */
	}
	public HashMap<Integer, HashMap<Integer, Integer>> relation_mots_page(Page p) 
	{
		HashMap<Integer, HashMap<Integer, Integer>> motsPage = new HashMap<>();
		for(String s: dico)
		{
			HashMap<Integer, Integer> occurByPage = new HashMap<Integer, Integer>();//id de la page, occurences du mot
			int indiceWord = dico.indexOf(s);
			if(isWordInPage(s, p))
			{
				occurByPage.put(p.getId(), p.wordOccurence(s));
			}
			motsPage.put(indiceWord, occurByPage);
		}
		return motsPage;
	}

	public static void main(String[] args) 
	{
		long tempsDebut = System.nanoTime(); 

		Collecteur collect = new Collecteur(getDataFromFile("dico"));
		
		collect.collect();
		
		long tempsFin = System.nanoTime(); 
		double seconds = (tempsFin - tempsDebut) / 1e9; 
		System.out.println("Fini en: "+ seconds + " secondes.");

		System.out.println("RAM : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) + " octets");

	}

}
