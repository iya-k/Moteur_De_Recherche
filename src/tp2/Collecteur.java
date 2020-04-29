package tp2;

import java.io.BufferedReader;
import java.util.HashMap;

import tp1.Page;
import utils.Commons;

public class Collecteur extends Commons
{
	BufferedReader buffer;
	
	
	
	public static void main(String[] args)
	{
		//Collecteur c = new Collecteur();
		System.out.println("Collecteur");
		//c.reading();
	}
	
	public void collect()
	{
		System.out.println("collect");

		mots_pages = new HashMap<>();
		for(String s: dictionary())
		{
			int indiceWord = dictionary().indexOf(s);
			HashMap<Integer, Integer> occurByPage = new HashMap<Integer, Integer>();	
			for(Page p: visited_pages)
			{
				if(isWordInPage(s, p))
				{
					occurByPage.put(p.getId(), wordOccurInPage(s, p));
				}
			}
			mots_pages.put(indiceWord, occurByPage);
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

	

}
