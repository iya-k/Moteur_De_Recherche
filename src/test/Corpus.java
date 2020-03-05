package test;

import java.io.*;
import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Corpus
{
	PrintWriter writer;
	private List<Element> pages;
	String category;
	Element racine;
	int size;
	
	public Corpus(String filter, Element root, List<Element> pages)
	{
		this.pages = pages;
		racine = root;
		category = filter;
		
	}

	void traitement()
	{

		writer = creerFichier("./resources/sortie.txt");
		//on recupère toutes les pages
		NodeList listPages = racine.getElementsByTagName("page");//getNodeName()) = page
		size = listPages.getLength();

		for(int iPage = 0; iPage < size; iPage++)
		{
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

								System.out.println(listPages.item(iPage).getNodeName());
								System.out.println("--"+contenuPage.item(iRevision).getNodeName());
								System.out.println("----"+text.getNodeName());

								pages.add((Element) listPages.item(iPage));

								//enregistreTxt(writer, listPages.item(iPage).getNodeName());
								//enregistreTxt(writer, contenuPage.item(iRevision).getNodeName());
								
								enregistreTxt(writer, text.getNodeName());
								enregistreTxt(writer, sansAccent(text.getTextContent()));

								/*
								String[] texts = text.getTextContent().split(" ");
								for(int indice = 0; indice < texts.length; indice++)
								{
									enregistreHashMap(texts[indice]);
								}
								 */

							}//fin if text
							

						}//for contenuRevision
					}
					else if(contenuPage.item(iRevision).getNodeName().equals("revision"))
					{
						//racine.removeChild(listPages.item(iPage));
					}
				}//for contenuPage

			}//fin if listPages.item(i)

		}//for listPages 
	}//traitement
	
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
	protected void enregistreTxt(PrintWriter writer, Object toSave)
	{
		writer.println(toSave);
		writer.flush();

	}
	protected String sansAccent (String s)
	{
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return (pattern.matcher(temp).replaceAll(""));//.replaceAll("\\p{P}\\p{S}","");
	}
	
}