package tp1;

import java.io.File;
import java.io.FileReader;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import utils.Commons;


public abstract class ProcessXML extends Commons
{

	static final int SIZE = 200000; 
	private String pathname;
	
	public ProcessXML(String path, String fileOut)
	{
		pathname = path;
	}

	public void loadDocument(String words_filter)
	{
		System.out.println("Chargement...");

		
		XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty("javax.xml.stream.isCoalescing", true);
        File filein = new File(pathname);
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
					case "title":
						xmlEvent = xmlEventReader.nextEvent();
                        if(xmlEvent instanceof Characters){
                            //writeTitle(xmlEvent.asCharacters().getData());
                        	titre = xmlEvent.asCharacters().getData();
                        }
						break;
					case "id" :
	               
	                    	if (ok)
	                    	{
	                    		break;
	                    	}
	                        xmlEvent = xmlEventReader.nextEvent();
	                        if(xmlEvent instanceof Characters) {
	                            //writeId(xmlEvent.asCharacters().getData());
	                        	id = xmlEvent.asCharacters().getData();
	                        	ok = true;
	                        }
	                    break;
					case "text":
	                        xmlEvent = xmlEventReader.nextEvent();
	                        if(xmlEvent instanceof Characters) 
	                        {
	                        	if(xmlEvent.asCharacters().getData().contains(words_filter))
	                        	{
	                        		//System.out.println("titre: "+titre+" id: "+id);
	                        		int taille = writeText(titre, id, xmlEvent.asCharacters().getData());
	                        		if (taille >= SIZE)
	                        		{
	                        			break;
	                        		}
	                        		titre = "";
	                        		id = "";
	                        		ok = false;
	                        	}
	                            
	                        }
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


    protected abstract int writeText(String title, String id, String text);
    protected abstract void endWritting();
}