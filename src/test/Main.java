package test;

public class Main {

	
	public static void main(String[] args) 
	{
		String path = "./resources/frwiki-debut.xml";//fichier source
		String outFile = "./resources/corpus.xml";//fichier destination
		String category = "sport";
		
		ProcessXML proc = new ProcessXML(path, outFile);//traitement du fichier
		Corpus corps = new Corpus(category, proc.loadDocument(path), proc.getPages());//nettoyage et selection
		corps.traitement();
		proc.writeXML();

	}
	   

}
