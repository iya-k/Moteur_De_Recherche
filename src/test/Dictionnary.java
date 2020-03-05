package test;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Dictionnary 
{

	private ArrayList<String> dico;
	private  LinkedHashMap<String, Integer> occurences;

	public Dictionnary()
	{

		dico = new ArrayList<>();
		occurences = new LinkedHashMap<>();
		
	}
	
	
	protected void enregistreDico(String chaine)
	{
		dico = (ArrayList<String>) occurences.keySet();
		dico.sort(String::compareToIgnoreCase);
		//String::compareToIgnoreCase
		System.out.println(dico.size());

	}
	protected void enregistreHashMap(String chaine)
	{
		System.out.println(chaine);
		if(occurences.containsKey(chaine.toLowerCase()))
		{
			occurences.replace(chaine, occurences.get(chaine), occurences.get(chaine)+1);
		}
		else
		{
			occurences.put(chaine, 1);
		}

	}
}
