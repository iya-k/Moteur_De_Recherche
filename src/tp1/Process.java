package tp1;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import utils.Commons;


public class Process extends Commons
{
	private int nbre_pages;
	final String PAGE = "page";
	final String TITLE = "title";
	final String ID = "id";
	final String TEXT = "text";
	private Page p;
	private Pattern pattern = Pattern.compile("\\[\\[([^\\]]*)\\|[^\\]]*\\]\\]");
	private static HashMap<Integer, Map<String, Integer>> words_by_page;//indice de la page, les mots et leurs occurences
	protected static ArrayList<String> liensParPage;

	public Process()
	{
		nbre_pages = 0;
	}

	public int traitement()
	{
		try {
			loadDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//collect();

		return nbre_pages;

	}//traitement

	public void loadDocument()
	{
		System.out.println("Chargement...");
		words_by_page = new HashMap<>();
		liensExternes = new HashMap<>();
		File filein = new File(CORPUS_XML_FILE);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setProperty("javax.xml.stream.isCoalescing", true);
		try {
			XMLEventReader xmlEventReader = factory.createXMLEventReader(new FileReader(filein));
			String titre = "";
			String id = "";
			while (xmlEventReader.hasNext()) 
			{
				XMLEvent xmlEvent = xmlEventReader.nextEvent();

				if(xmlEvent.isStartElement())
				{
					StartElement startElement = xmlEvent.asStartElement();
					switch (startElement.getName().getLocalPart()) {
					case TITLE:
						xmlEvent = xmlEventReader.nextEvent();
						if(xmlEvent instanceof Characters){
							titre = xmlEvent.asCharacters().getData();
						}
						break;
					case ID :

						xmlEvent = xmlEventReader.nextEvent();
						
						if(xmlEvent instanceof Characters) {
							id = xmlEvent.asCharacters().getData();
							//System.out.println("--------id: "+id);
						}
						break;
					case TEXT:
						xmlEvent = xmlEventReader.nextEvent();

						if(xmlEvent instanceof Characters) 
						{
								String text = xmlEvent.asCharacters().getData();
									//titre = stripAccents(titre);
									//System.out.println("titre: "+titre+", id: "+id+" finnnnnnnnnnnnnnnnnn \n\n");
									writeText(titre, id, text);
									
									p = new Page(Integer.parseInt(id));
									p.setTitle(titre);
									Page newPage = stripAccentsAndCount(text, p);
									//System.out.println("\n\n ----------------"+nbre_pages);
									words_by_page.put(newPage.getId(), newPage.getOccur_Mots());
									visited_pages.add(newPage);
								
						}//if xmlEvent
						break;
					default:

						break;
					}
				}
				else if(xmlEvent.isEndDocument())
				{
					//endWritting();
				}
			}
			addPageLinks();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Fin du chargement");
	}

	public int writeText(String title, String id, String text) 
	{
		nbre_pages++;
		liensParPage = new ArrayList<String>();
		
		//pour extaire les liens
		Matcher matcher = pattern.matcher(text);
		
		while(matcher.find())
		{
			String link = matcher.group(1);
			if(!liensParPage.contains(link))
			{
				liensParPage.add(link);
			}	
		}
		liensExternes.put(Integer.parseInt(id), liensParPage);
		return nbre_pages;
	}
	
	public void addPageLinks()

	{
		for(Entry<Integer, ArrayList<String>> map : liensExternes.entrySet())
		{
			for (Page p: visited_pages)
			{
				if(p.getId() == map.getKey())
				{
					p.setLinks(map.getValue());
					
				}
			}
		}
	}
	/**
	 * @return the nbre_pages
	 */
	public int getNbre_pages() {
		return nbre_pages;
	}

	public static void main(String[] args) 
	{
		long tempsDebut = System.nanoTime(); 

		System.out.println(new Process().traitement());//nettoyage et selection
		
		for(Page p: visited_pages)
		{
			System.out.println(p.toString());
		}
		
		long tempsFin = System.nanoTime(); 
		double seconds = (tempsFin - tempsDebut) / 1e9; 
		System.out.println("Fini en: "+ seconds + " secondes.");

		System.out.println("RAM : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) + " octets");

	}
}