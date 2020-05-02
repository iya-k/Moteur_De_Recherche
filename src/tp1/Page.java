package tp1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Page 
{
	private Integer id;
	private String title;
	private Map<String, Integer> occur_mots;
	private double rank;
	private ArrayList<String> links;
	
	public Page(Integer id) 
	{
		this.id = id;
		rank=0.0;
		occur_mots = new HashMap<String, Integer>();
		links = new ArrayList<String>();
	}
	public Integer getId() 
	{
		return id;
	}
	public void setId(Integer id) 
	{
		this.id = id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	public double getRank() 
	{
		return rank;
	}

	/**
	 * @return the mots
	 */
	public Map<String, Integer> getOccur_Mots() {
		return occur_mots;
	}
	/**
	 * @param mots the mots to set
	 */
	public void setOccur_Mots(Map<String, Integer> mots) {
		this.occur_mots = mots;
	}
	/**
	 * @return the links
	 */
	public ArrayList<String> getLinks() {
		return links;
	}
	/**
	 * @param links the links to set
	 */
	public void setLinks(ArrayList<String> links) {
		this.links = links;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(double rank) {
		this.rank = rank;
	}
	
	/**
	 * @return the title
	 */
	public String getTitleByID(int id) 
	{
		return title;
	}
	
	/**
	 * @return the title
	 */
	public int getIDByTitle(String title) 
	{
		return id;
	}
	
	public int wordOccurence(String word) 
	{
		if(occur_mots.containsKey(word))
		{
			return occur_mots.get(word);
		}
		return 0;
	}
	
	public String listMots() 
	{
		String toReturn = "";
		for(Entry<String, Integer> mapping : occur_mots.entrySet())
		{
			//System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
			toReturn += mapping.getKey() + " ==> " + mapping.getValue()+" \t";
		}
		return toReturn;
	}
	
	public String toString() 
	{
		return "Page: "+id+" - "+title+"\n"
				+ "l'ensemble des liens:\n" + links;
	}
	
	/*
     * Comparator pour le tri des page par rank 
     */
    public static Comparator<Page> ComparatorAge = new Comparator<Page>() {
     
        @Override
        public int compare(Page p1, Page p2) 
    	{
    		return (int) (p1.getRank() - p2.getRank());
    		
    	}
    };
	
	

}
