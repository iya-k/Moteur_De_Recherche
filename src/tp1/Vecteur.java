package tp1;

public class Vecteur 
{
	private int n;
	private float[] tab;

	public Vecteur(int size, float[] tableau)
	{
		n = size;
		tab = tableau;
		
	}
	public Vecteur(int size, float value)
	{
		n = size;
		tab = new float[size];
		for(int i = 0; i < n; i++)
		{
			tab[i] = value;
		}
		
	}
	public Vecteur(float[] tableau)
	{
		tab = tableau;
		n = tableau.length;
	}
	
	/**
	 * @return the n
	 */
	public int getN() 
	{
		return n;
	}
	/**
	 * @return the tab
	 */
	public float[] getTab() 
	{
		return tab;
	}
	
	public String toString() 
	{
        String str = "";
        for (int i = 0; i < tab.length; i++) 
        {
            str += tab[i];
            str += (i == tab.length - 1) ? "" : ", ";
        }
        return "Vecteur: [ " + str + "]";
    }

	
	
	public static void main(String[] args) 
	{
        float[] vector = {2, 5, 7, 8};
        Vecteur v = new Vecteur(vector);
        
        System.out.print("\n");
        System.out.println(v.toString());
        //System.out.println(v.produit_matrice(m).toString());
    }
}
