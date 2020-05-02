package tp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class Corpus
{
	static final int MAX_SIZE = 100;//200000; 
	public static final String[] CATEGORY =  {" sport", " tenis", " judo", " football"};//category pour le filtrer
	public static final String PATH = "./resources/frwiki-debut.xml";//fichier source
	public static final String CORPUS_XML_FILE = "./resources/corpus.xml";//fichier destination
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
	PrintWriter writer;

	public Corpus(String path, String fileOut)
	{
		pathname = path;
		outFile = fileOut;
		writer = createFile("./resources/sortie.txt");
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
			writeStartElement(out, RACINE);
			writeStartElement(out, PAGES);
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

			writer.println(title);
			writer.flush();
			
			writeStartElement(out, ID);
			out.writeCharacters(id);
			out.writeEndElement();

			writeStartElement(out, TEXT);
			out.writeCharacters(text);
			writeEndElement(out);
			writer.println(text);
			writer.flush();

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
	protected void writeStartElement(XMLStreamWriter out, String toSave)
	{
		try {
			out.writeCharacters("\n");
			out.writeStartElement(toSave);
			out.flush();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

	}
	protected void writeEndElement(XMLStreamWriter out)
	{
		try {
			out.writeCharacters("\n");
			out.writeEndElement();
			out.flush();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	//creation d'un fichier
			protected PrintWriter createFile(String path)
			{
				PrintWriter wr = null;
				try {
					wr = new PrintWriter(path);
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				}
				System.out.println("fichier creer "+path);
				return  wr;
			}
	
	public static void main(String[] args) 
	{
		long tempsDebut = System.nanoTime(); 

		System.out.println(new Corpus(PATH, CORPUS_XML_FILE).traitement());//nettoyage et selection

		long tempsFin = System.nanoTime(); 
		double seconds = (tempsFin - tempsDebut) / 1e9; 
		System.out.println("Fini en: "+ seconds + " secondes.");

		System.out.println("RAM : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) + " octets");

	}
}