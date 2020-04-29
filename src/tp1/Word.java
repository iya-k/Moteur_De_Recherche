package tp1;

import java.util.ArrayList;
import java.util.Comparator;

public class Word implements Comparable<Word>
{
	private String word;
	private int nbrOccurence;
	private ArrayList<Page> pages;

    private String newWord;
    private String regionSecond = "";
    private String regionfirst = "";
    private String region = "";
	
	public Word(Page p, String s, int nbOccur)
	{
		this.pages = new ArrayList<>();
		this.pages.add(p);
		word = s;
		nbrOccurence = nbOccur;
		
	}

	public Word(Page p, String s, ArrayList<Page> pages, int nbOccur)
	{
		this.pages = new ArrayList<>();
		 this.pages=pages;
		 if(!pages.contains(p))
			 pages.add(p);
		word = s;
		
	}	
	
	public Word(String word) 
	{
        assert word != null;
        this.word = word.toLowerCase();
        this.newWord = word;
	}
	
	/**
	 * @return the mot
	 */
	public String getWord() {
		return word;
	}
	/**
	 * @param mot the mot to set
	 */
	public void setWord(String mot) {
		this.word = mot;                                        
	}
	/**
	 * @return the nbrOccurence
	 */
	public int getNbrOccurence() {
		return nbrOccurence;
	}
	/**
	 * @param nbrOccurence the nbrOccurence to set
	 */
	public void setNbrOccurence(int nbrOccurence) {
		this.nbrOccurence = nbrOccurence;
	}
	/**
	 * @return the pages
	 */
	public ArrayList<Page> getPages() {
		return pages;
	}
	/**
	 * @param pages the pages to set
	 */
	public void setPages(ArrayList<Page> pages) {
		this.pages = pages;
	}
	
	/**
	 * @return the newWord
	 */
	public String getNewWord() {
		return newWord;
	}
	/**
	 * @param newWord the newWord to set
	 */
	public void setNewWord(String newWord) {
		this.newWord = newWord;
	}
	/**
	 * @return the regionSecond
	 */
	public String getRegionSecond() {
		return regionSecond;
	}
	/**
	 * @param regionSecond the regionSecond to set
	 */
	public void setRegionSecond(String regionSecond) {
		this.regionSecond = regionSecond;
	}
	/**
	 * @return the regionfirst
	 */
	public String getRegionfirst() {
		return regionfirst;
	}
	/**
	 * @param regionfirst the regionfirst to set
	 */
	public void setRegionfirst(String regionfirst) {
		this.regionfirst = regionfirst;
	}
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	public String toString() 
	{
		return " [ mot=" + word +", occurence=" + nbrOccurence + ", pages=" + pages +  "]";
	}
	public static void main(String[] args) 
	{

	}


	@Override
	public int compareTo(Word w1) 
	{
		return (this.getNbrOccurence() - w1.getNbrOccurence());
	}

}
