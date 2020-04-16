package tp1;


import java.io.*;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Corpus extends ProcessXML
{
	PrintWriter writer;
    private XMLStreamWriter out;
    String outFile;
    int taille = 0;
    String category;
	
	public Corpus(String pathname, String fileOut)
	{
		super(pathname, fileOut);
		outFile = fileOut;
		writer = super.createFile("./resources/sortie.txt");

		System.out.println("Corpus");
	}

	public int traitement(String filter)
	{
		System.out.println("traitement");
		
	        File file = new File(outFile);
	        this.category = filter;
	        try {
	            OutputStream outputStream = new FileOutputStream(file);
	            out = XMLOutputFactory.newInstance().createXMLStreamWriter(
	                    new OutputStreamWriter(outputStream, "utf-8"));
	            out.writeStartDocument();
	            out.writeStartElement("pages");
	            super.loadDocument(filter);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
		return taille;
		
	}//traitement
	/*
	@Override
    public void writeTitle(String title) {
        try {
            taille++;
            out.writeStartElement("page");
            out.writeStartElement("title");
            out.writeCharacters(title);
            out.writeEndElement();
            writer.write(title);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    //TODO filtrer avec les words_filter
    @Override
    public void writeId(String id) {
        try{
            out.writeStartElement("id");
            out.writeCharacters(id);
            out.writeEndElement();
        }catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }
    */
    @Override
    public int writeText(String title, String id, String text) 
    {
        try{
        	super.writeStartElement(out, "page");
            //out.writeStartElement("page");
        	super.writeStartElement(out,"title");
            out.writeCharacters(title);
            out.writeEndElement();
            super.stringToWrite(writer,title);
            
            super.writeStartElement(out,"id");
            out.writeCharacters(id);
            out.writeEndElement();
            
            super.writeStartElement(out,"text");
            out.writeCharacters(text);
            super.writeEndElement(out);
            super.writeEndElement(out);
            
            taille++;
        }catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return taille;
    }

    @Override
    public void endWritting() 
    {
        try{
        	super.writeEndElement(out);
            out.writeEndDocument();
            out.close();
            writer.close();
        }catch (XMLStreamException e){
            e.printStackTrace();
        }
    }

	public static void main(String[] args) 
	{
		String path = "./resources/frwiki-debut.xml";//fichier source
		String outFile = "./resources/corpus.xml";//fichier destination
		String category = "sport";
		long tempsDebut = System.nanoTime(); 
		
		System.out.println(new Corpus(path, outFile).traitement(category));//nettoyage et selection

		long tempsFin = System.nanoTime(); 
		double seconds = (tempsFin - tempsDebut) / 1e9; 
		System.out.println("Fini en: "+ seconds + " secondes.");
		
		//Dictionnary dico = new Dictionnary();
		//dico.generateDictionnary("./resources/sortie.txt");
		System.out.println("RAM : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) + " octets");

	}
	
	/*
	 * java Corpus 
fichier creer ./sortie.txt
Corpus
traitement
Chargement...
javax.xml.stream.XMLStreamException: ParseError at [row,col]:[120269336,401]
Message: JAXP00010004: The accumulated size of entities is "50,000,001" that exceeded the "50,000,000" limit set by "FEATURE_SECURE_PROCESSING".
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl.next(XMLStreamReaderImpl.java:652)
	at java.xml/com.sun.xml.internal.stream.XMLEventReaderImpl.nextEvent(XMLEventReaderImpl.java:83)
	at ProcessXML.loadDocument(ProcessXML.java:61)
	at Corpus.traitement(Corpus.java:38)
	at Corpus.main(Corpus.java:95)
Fin du chargement
177262
Fini en: 102.985632307 secondes.
RAM : 93762896 octets
	 * */
	
	
}