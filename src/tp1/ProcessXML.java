package tp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import utils.Commons;


public abstract class ProcessXML extends Commons
{
	private Element root;
	private Document xmlSortie ;
	private Document xmlDocument;
	private DocumentBuilder builder;

	//private List<Element> pages;
	private String pathname, outFile;
	
	public ProcessXML(String path, String fileOut)
	{
		pathname = path;
		outFile = fileOut;
		//pages = new ArrayList<>();
	}
	
	/**
	 * @return the pages
	 *
	public List<Element> getPages() 
	{
		return pages;
	}*/
	public Element loadDocument()
	{

		System.out.println("Chargement...");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			//factory.setValidating(true);
			// Création de notre parseur via la factory
			builder = factory.newDocumentBuilder();
			File fileXML = new File(pathname);
			
			// parsing de notre fichier via un objet File et récupération d'un objet Document
			// Ce dernier représente la hiérarchie d'objet créée pendant le parsing
			xmlDocument = builder.parse(fileXML);

			// Via notre objet Document, nous pouvons récupérer un objet Element
			// Ce dernier représente un élément XML mais, avec la méthode ci-dessous, cet élément sera la racine du document
			root = xmlDocument.getDocumentElement();


		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.out.println("Erreur ParserConfiguration");
		} catch (FileNotFoundException e) {
			System.out.println("Erreur Création du fichier");
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println("builder.parse(fileXML)");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("builder.parse(fileXML)");
			e.printStackTrace();
		}
		System.out.println("Fin du chargement");
		return root;
	}


	public void createXMLDocument(List<Element> pages)
	{
		xmlSortie = builder.newDocument();
		
		xmlSortie.setXmlVersion("1.0");
		xmlSortie.setXmlStandalone(true);
		xmlSortie.appendChild(xmlSortie.createElement("mediawiki"));
		int i = 0;
		System.out.println("Taille des pages "+pages.size());
		while(i < pages.size())
		{
			Node copyPage = xmlSortie.importNode(pages.get(i), true);
			xmlSortie.getDocumentElement().appendChild(copyPage);
			i++;
		}

	}
	protected void writeXML(List<Element> pages)
	{
		System.out.println("XML file: "+outFile);
		createXMLDocument(pages);
		
		//write the new document in a XML file
		try {
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transf = transFactory.newTransformer();
			DOMSource source = new DOMSource(xmlSortie);
			StreamResult result = new StreamResult(new File(outFile));

			transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transf.setOutputProperty(OutputKeys.INDENT, "yes");
			transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			
			transf.transform(source, result);

		} catch (TransformerConfigurationException e) {
			System.out.println("transFactory.newTransformer()");
			e.printStackTrace();
		} catch (TransformerException e) {
			System.out.println("Erreur transform.transform");
			e.printStackTrace();
		}

	}

}