package tp1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import utils.Commons;


public class Corpus extends Commons
{
	static final int MAX_SIZE = 100;//200000; 
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

	public Corpus(String path, String fileOut)
	{
		pathname = path;
		outFile = fileOut;
	}
	
	public int traitement()
	{
		nbre_pages = 0;
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
		
		return nbre_pages;

	}//traitement

	public void loadDocument()
	{
		
		System.out.println("Chargement...");
		File filein = new File(pathname);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setProperty("javax.xml.stream.isCoalescing", true);
		try {
			XMLEventReader xmlEventReader = factory.createXMLEventReader(new FileReader(filein));
			String titre = "";
			String id = "";
			boolean ok = false;
			boolean maxAtteind = false;
			while (xmlEventReader.hasNext()) {
				
				if(maxAtteind)
				{
					break;
				}
				XMLEvent xmlEvent = xmlEventReader.nextEvent();

				if(xmlEvent.isStartElement())
				{
					StartElement startElement = xmlEvent.asStartElement();
					switch (startElement.getName().getLocalPart()) {
					case TITLE:

	                    //System.out.println(page_vu+" nbr de conforme: "+nbre_pages);
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
						if(titre.equals(""))
						{
							break;
						}
						if(xmlEvent instanceof Characters) 
						{
							for(String cat: CATEGORY)
							{
								//System.out.println(cat);
								String text = xmlEvent.asCharacters().getData();
								if(text.contains(cat))
								{
									titre = stripAccents(titre);
									text = xmlEvent.asCharacters().getData();
									//System.out.println("titre: "+titre+", id: "+id+" finnnnnnnnnnnnnnnnnn \n\n");
                                    if ( writeText(titre, id, text) >= MAX_SIZE)
									{
                                    	maxAtteind = true;
                                    	System.out.println(maxAtteind);
										break;
									}
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

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Fin du chargement");
	}

	public int writeText(String title, String id, String text) 
	{
		try{
			writeStartElement(out, PAGE);
			writeStartElement(out, TITLE);
			out.writeCharacters(title);
			out.writeEndElement();

			writeStartElement(out, ID);
			out.writeCharacters(id);
			out.writeEndElement();

			writeStartElement(out, TEXT);
			out.writeCharacters(text);
			writeEndElement(out);
			//super.stringToWrite(writer,text);

			writeEndElement(out);

		}catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		return nbre_pages++;
	}

	public void endWritting() 
	{
		try{
			writeEndElement(out);
			writeEndElement(out);
			out.writeEndDocument();
			out.close();
			//writer.close();
		}catch (XMLStreamException e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) 
	{
		long tempsDebut = System.nanoTime(); 

		System.out.println(new Corpus(PATH, OUT_XML_FILE).traitement());//nettoyage et selection

		long tempsFin = System.nanoTime(); 
		double seconds = (tempsFin - tempsDebut) / 1e9; 
		System.out.println("Fini en: "+ seconds + " secondes.");

		System.out.println("RAM : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) + " octets");

	}
}