package test;

import java.io.*;
import java.text.Normalizer;
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
	int size;
	String filter = "sport";
	String delim = "-----------------------------------------------------------------";
	
	public Corpus(String pathname, String fileOut)
	{
		super(pathname, fileOut);
		
		pages = super.getPages();
		racine = super.loadDocument();
		category = filter;
	}

	public void traitement()
	{

		writer = super.createFile("./resources/sortie.txt");
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
								pages.add((Element) listPages.item(iPage));

								super.stringToWrite(writer, listPages.item(iPage).getFirstChild().getNextSibling().getTextContent());
								super.stringToWrite(writer, text.getTextContent());
								//enregistreTxt(writer, delim);

							}//fin if text
							

						}//for contenuRevision
					}
				}//for contenuPage

			}//fin if listPages.item(i)

		}//for listPages 
		
		super.writeXML();
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
	
}