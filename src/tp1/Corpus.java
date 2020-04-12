package tp1;


import java.io.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Corpus extends ProcessXML
{
	PrintWriter writer;
	private List<Element> pages;
	String category;
	Element racine;
	static final int SIZE = 200000; 
	String filter = "sport";
	String delim = "-----------------------------------------------------------------";
	
	public Corpus(String pathname, String fileOut)
	{
		super(pathname, fileOut);
		
		pages = new ArrayList<>();
		racine = super.loadDocument();
		category = filter;

		System.out.println("Corpus");
	}

	public void traitement()
	{

		System.out.println("traitement");
		writer = super.createFile("./resources/sortie.txt");
		//on recupère toutes les pages
		NodeList listPages = racine.getElementsByTagName("page");//getNodeName()) = page
		//SIZE = listPages.getLength();

		//for(int iPage = 0; iPage < SIZE; iPage++)
		int iPage = 0;
		int cpt = 0;
		//System.out.println(iPage);
		while(cpt < SIZE && iPage < listPages.getLength())
		{
			//System.out.println(iPage);
			if(listPages.item(iPage) instanceof Element)
			{
				NodeList contenuPage = listPages.item(iPage).getChildNodes();
				
				for(int iRevision = 0; iRevision < contenuPage.getLength(); iRevision++)
				{
					if((contenuPage.item(iRevision) instanceof Element) && contenuPage.item(iRevision).getNodeName().equals("revision"))
					{
						NodeList contenuRevision = contenuPage.item(iRevision).getChildNodes();
						for(int iText = 0; iText < contenuRevision.getLength(); iText++)
						{
							Node text = contenuRevision.item(iText); 
							if((text instanceof Element) && 
									text.getNodeName().equals("text") && (text.getTextContent().contains(category))) 
							{	
								pages.add((Element) listPages.item(iPage));

								super.stringToWrite(writer, listPages.item(iPage).getFirstChild().getNextSibling().getTextContent());
								//super.stringToWrite(writer, text.getTextContent());
								//enregistreTxt(writer, delim);
								
								cpt++;

							}//fin if text
						}//for contenuRevision
					}//fin if
				}//for contenuPage

			}//fin if listPages.item(i)
			iPage++;

		}//for listPages 
		
		super.writeXML(pages);
		writer.close();
		
	}//traitement
	/*
	protected PrintWriter creerFichier(String path)
	{
		PrintWriter wr = null;
		try {
			wr = new PrintWriter(path);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		return  wr;
	}
	
	protected void enregistreTxt(PrintWriter writer, String toSave)
	{
		writer.println(toSave);
		writer.flush();

	}*/
	
	public static void main(String[] args) 
	{
		String path = "./resources/frwiki-debut.xml";//fichier source
		String outFile = "./resources/corpus.xml";//fichier destination
		String category = "sport";

		System.out.println("main");
		long tempsDebut = System.nanoTime(); 
		
		new Corpus(path, outFile).traitement();//nettoyage et selection

		long tempsFin = System.nanoTime(); 
		double seconds = (tempsFin - tempsDebut) / 1e9; 
		System.out.println("Opération effectuée en: "+ seconds + " secondes.");
		
		//Dictionnary dico = new Dictionnary();
		//dico.generateDictionnary("./resources/sortie.txt");
		System.out.println("Mémoire allouée : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) + " octets");

	}
	
	
}