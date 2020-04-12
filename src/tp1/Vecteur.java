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
	
	public void print_vector(Vecteur vector) 
	{
        String str = "";
        for (int i = 0; i < vector.tab.length; i++) 
        {
            str += vector.tab[i];
            str += (i == vector.tab.length - 1) ? "" : ", ";
        }
        System.out.println("[ " + str + "]");
    }

	//produit de la matrice
	public Vecteur produit_matrice(float[] vector, Matrice m) 
	{
		float[] res = new float[n];
		
		for (int i = 0, k = 1; k < m.getmL().size(); i++, k++) 
		{
			for (float j = m.getmL().get(i); j < m.getmL().get(k); j++)
			{	
				//System.out.println("indice vector = "+ m.getmI().get((int) j));
				res[i] +=  m.getmC().get((int) j) * vector[m.getmI().get((int) j)];
				//System.out.println(res[i] );
			}
		}
		return new Vecteur(res);

	}

	//matrice transposée
	public Vecteur vector_transpose(float[] vector, Matrice m) 
	{
		float[] res = new float[vector.length];
		for (int i = 1; i <  m.getmL().size(); i++) 
        {
            for (float j = m.getmL().get(i - 1); j < m.getmL().get(i); j++) 
            {
            	res[m.getmI().get((int) j)] += m.getmC().get((int) j) * vector[i - 1]; // On effectue les calculs non plus sur les colonnes de la matrice mais sur ses lignes
            	
            }
        }

		return new Vecteur(res);
	}

	// après avoir calculé le produit par la transposée, on modifie chaque valeur du vecteur afin de prendre en compte le facteur de zap
	public Vecteur vector_transpose_zap(Vecteur vector, float zap) {
		float[] result = vector.tab;

        for (int i = 0; i < result.length; i++) 
        {
        	result[i] *= (1 - zap) + (zap / this.n);// On effectue les calculs non plus sur les colonnes de la matrice mais sur ses lignes
        }

        return new Vecteur(result);
    }
	
	public static void main(String[] args) 
	{
        Integer[] L = {0, 3, 5, 5, 6};
        Float[] C = {3f, 5f, 8f, 1f, 2f, 3f};
        Integer[] I = {1, 2, 3, 0, 2, 1};
        Matrice m = new Matrice(C, L, I);


        float[] vector = {2, 5, 7, 8};
        Vecteur v = new Vecteur(vector);

        System.out.println("\nMatrice CLI: " + m.toString());
        System.out.print("Vecteur : ");
        v.print_vector(v);
        System.out.print("Resultat : ");
        v.print_vector(v.produit_matrice(vector, m));
        System.out.println();
		
		
		
	}
}
