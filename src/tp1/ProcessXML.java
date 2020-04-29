package tp1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import utils.Commons;


public class ProcessXML extends Commons
{
	static final int MAX_SIZE = 200000; 
	private String pathname;
	private int nbre_pages;
	private XMLStreamWriter out;
	private String outFile;
	final String PAGES = "pages";
	final String RACINE = "mediawiki";
	final String PAGE = "page";
	final String TITLE = "title";
	final String ID = "id";
	final String TEXT = "text";
	private Page p;
	private Pattern pattern = Pattern.compile("\\[\\[([^\\]]*)\\|[^\\]]*\\]\\]");
	private static HashMap<Integer, Map<String, Integer>> words_by_page;//indice de la page, les mots et leurs occurences
	protected static ArrayList<String> liensParPage;

	public ProcessXML(String path, String fileOut)
	{
		nbre_pages = 0;
		pathname = path;
		outFile = fileOut;
	}

	public int traitement()
	{
		File file = new File(outFile);
		try {
			OutputStream outputStream = new FileOutputStream(file);
			out = XMLOutputFactory.newInstance().createXMLStreamWriter(
					new OutputStreamWriter(outputStream, "utf-8"));
			out.writeStartDocument();
			super.writeStartElement(out, RACINE);
			super.writeStartElement(out, PAGES);
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
		File filein = new File(pathname);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setProperty("javax.xml.stream.isCoalescing", true);
		try {
			XMLEventReader xmlEventReader = factory.createXMLEventReader(new FileReader(filein));
			String titre = "";
			String id = "";
			boolean ok = false;
			while (xmlEventReader.hasNext()) {
				XMLEvent xmlEvent = xmlEventReader.nextEvent();

				if(xmlEvent.isStartElement())
				{
					StartElement startElement = xmlEvent.asStartElement();
					switch (startElement.getName().getLocalPart()) {
					case TITLE:
						xmlEvent = xmlEventReader.nextEvent();
						if(xmlEvent instanceof Characters){
							titre = xmlEvent.asCharacters().getData();
							//System.out.println("titre: "+titre);
						}
						break;
					case ID :

						xmlEvent = xmlEventReader.nextEvent();
						if (ok)
						{
							break;
						}
						if(xmlEvent instanceof Characters) {
							id = xmlEvent.asCharacters().getData();
							ok = true;
							//System.out.println("--------id: "+id);
						}
						break;
					case TEXT:
						xmlEvent = xmlEventReader.nextEvent();

						if(xmlEvent instanceof Characters) 
						{
							for(String cat: CATEGORY)
							{
								//System.out.println(cat);
								String text = xmlEvent.asCharacters().getData();
								if(text.contains(cat))
								{
									if(titre.equals(""))
									{
										break;
									}
									titre = stripAccents(titre);
									text = xmlEvent.asCharacters().getData();
									//System.out.println("titre: "+titre+", id: "+id+" finnnnnnnnnnnnnnnnnn \n\n");
						
									//int taille = writeText(titre, id, text);
									
									if (writeText(titre, id, text) >= MAX_SIZE)
									{
										break;
									}
									p = new Page(Integer.parseInt(id));
									p.setTitle(titre);
									Page newPage = stripAccentsAndCount(titre+" "+text, p);
									//System.out.println(" nouvelle page finnnnnnnnnnnnnnnnnn \n\n");
									words_by_page.put(newPage.getId(), newPage.getOccur_Mots());
									getListPages().add(newPage);
									
									titre = "";
									id = "";
									//System.out.println(p.toString());
								}
								ok = false;
							} //for words_filter
						}//if xmlEvent
						break;
					default:

						break;
					}
				}
				else if(xmlEvent.isEndDocument()){
					endWritting();
				}
			}
			//collect();
			dictionary();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Fin du chargement");
	}

	public int writeText(String title, String id, String text) 
	{
		liensParPage = new ArrayList<String>();
		try{
			super.writeStartElement(out, PAGE);
			super.writeStartElement(out, TITLE);
			out.writeCharacters(title);
			out.writeEndElement();
			//super.stringToWrite(writer,title);

			super.writeStartElement(out, ID);
			out.writeCharacters(id);
			out.writeEndElement();

			super.writeStartElement(out, TEXT);
			out.writeCharacters(text);
			super.writeEndElement(out);
			//super.stringToWrite(writer,text);

			super.writeEndElement(out);

			nbre_pages++;
		}catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		//pour extaire les liens
		Matcher matcher = pattern.matcher(text);
		
		while(matcher.find())
		{
			liensParPage.add(matcher.group(1));
				
		}
		liensExternes.put(Integer.parseInt(id), liensParPage);
		return nbre_pages;
	}

	public void endWritting() 
	{
		try{
			super.writeEndElement(out);
			super.writeEndElement(out);
			out.writeEndDocument();
			out.close();
			//writer.close();
		}catch (XMLStreamException e){
			e.printStackTrace();
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

		System.out.println(new ProcessXML(PATH, OUT_XML_FILE).traitement());//nettoyage et selection

		long tempsFin = System.nanoTime(); 
		double seconds = (tempsFin - tempsDebut) / 1e9; 
		System.out.println("Fini en: "+ seconds + " secondes.");

		System.out.println("RAM : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) + " octets");

	}
}