package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Traitement {
	private static Element racine;
	private static Element racineSortie;
	static Document xmlSortie ;
	static OutputStream out;
	static PrintWriter writer;
    private static ArrayList<String> dico;
    private static LinkedHashMap<String, Integer> occurences;

   public static void main(String[] args) {
      // Nous récupérons une instance de factory qui se chargera de nous fournir
      // un parseur
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      dico = new ArrayList<>();
      occurences = new LinkedHashMap<>();

      try {
         // Création de notre parseur via la factory
         DocumentBuilder builder = factory.newDocumentBuilder();
         File fileXML = new File("frwiki-debut.xml");
         
         System.out.println(fileXML);
         // parsing de notre fichier via un objet File et récupération d'un
         // objet Document
         // Ce dernier représente la hiérarchie d'objet créée pendant le parsing
         Document xml = builder.parse(fileXML);

         // Via notre objet Document, nous pouvons récupérer un objet Element
         // Ce dernier représente un élément XML mais, avec la méthode ci-dessous,
         // cet élément sera la racine du document
         racine = xml.getDocumentElement();

         xmlSortie = builder.newDocument();
         racineSortie = xmlSortie.getDocumentElement();
         
         //newRacine = sortie.createElement("mediawiki");
         //sortie.appendChild(newRacine);
         
		 //out = new FileOutputStream("fileXML.xml");
		 writer = creerFichier("sortie.txt");
         if(fileXML != null) {

             traitement();
         }
         
         //serializetoXML(out, "utf-8", sortie);
         
         //afficheALL(taille);

      } catch (ParserConfigurationException e) 
      {
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
   }
   
   static void traitement()
   {
	   NodeList list = racine.getElementsByTagName("page");//getNodeName()) = page
	   
	   for(int i = 0; i < list.getLength(); i++)
	   {
		   Node page = list.item(i).cloneNode(true);
		   NodeList contenuPage = page.getChildNodes();
		   for(int j = 0; j < contenuPage.getLength(); j++)
		   {
			   Node revision = contenuPage.item(j).cloneNode(true);
			   if(revision.getNodeName().equals("revision")) 
			   {
				   NodeList contenuRevision = revision.getChildNodes();
				   for(int t = 0; t < contenuRevision.getLength(); t++)
				   {
					   Node text = contenuRevision.item(t).cloneNode(true); 
					   if(text.getNodeName().equals("text") && (text.getTextContent().contains(" Sport") 
							   || text.getTextContent().contains(" sport"))) 
					   {
							
							  System.out.println(list.item(i).getNodeName());
							  System.out.println("--"+revision.getNodeName());
							  System.out.println("----"+text.getNodeName());
							 
						   
						   //newRacine.appendChild(page);
						   
						   racineSortie.appendChild(page);
						   	enregistreTxt(writer, page.getNodeName());
							enregistreTxt(writer, revision.getNodeName());
							enregistreTxt(writer, text.getNodeName());
							enregistreTxt(writer, text.getTextContent());
							/*
							String[] texts = text.getTextContent().split(" ");
							for(int indice = 0; indice < texts.length; indice++)
							{
								enregistreHashMap(texts[indice]);
							}
						   */
						   
					   }
					  
				   }//for contenuRevision
			   }
		   }//for contenuPage
		   
	   }//for list 
	   
   }
   
	static void enregistre(String fichier)
	{
		//On utilise ici un affichage classique avec getPrettyFormat()
		//XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		//Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
		//avec en argument le nom du fichier pour effectuer la sérialisation.
		
		//out.output(sortie, new FileOutputStream(fichier));
	}
	static PrintWriter creerFichier(String path)
	{
		PrintWriter wr = null;
		try {
			wr = new PrintWriter(path);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		return  wr;
	}
	static void enregistreTxt(PrintWriter writer, Object toSave)
	{
		writer.println(toSave);
		writer.flush();

	}

	static void enregistreDico(String chaine)
	{
		dico = (ArrayList<String>) occurences.keySet();
		dico.sort(String::compareToIgnoreCase);
		//String::compareToIgnoreCase
		System.out.println(dico.size());

	}
	static void enregistreHashMap(String chaine)
	{
		System.out.println(chaine);
		if(occurences.containsKey(chaine.toLowerCase()))
		{
			occurences.replace(chaine, occurences.get(chaine), occurences.get(chaine)+1);
		}
		else
		{
			occurences.put(chaine, 1);
		}
		
	}
	
}