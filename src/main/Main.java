package main;

import test.Corpus;
import test.Dictionnary;
import test.ProcessXML;
import utils.Commons;

public class Main extends Commons
{	
	public static void main(String[] args) 
	{
		String path = "./resources/frwiki-debut.xml";//fichier source
		String outFile = "./resources/corpus.xml";//fichier destination
		String category = "sport";
		Corpus corps = new Corpus(path, outFile);//nettoyage et selection
		//Dictionnary dico = new Dictionnary();
		//dico.generateDictionnary("./resources/sortie.txt");

	}
	   

}
