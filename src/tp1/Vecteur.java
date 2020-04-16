package tp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

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
	public Vecteur produit_matrice(Vecteur vector, Matrice m) 
	{
		float[] res = new float[n];
		
		for (int i = 0, k = 1; k < m.getmL().size(); i++, k++) 
		{
			for (float j = m.getmL().get(i); j < m.getmL().get(k); j++)
			{	
				//System.out.println("indice vector = "+ m.getmI().get((int) j));
				res[i] +=  m.getmC().get((int) j) * vector.tab[m.getmI().get((int) j)];
				//System.out.println(res[i] );
			}
		}
		return new Vecteur(res);

	}
	//[ 4.0, 41.0, 45.0, 57.0]
	/*
	public Vecteur vector_transpose(Vecteur vector, Matrice m) 
	{
		float[] res = new float[vector.tab.length];
		for (int i = 0, k = 1, indice = 0; k <  m.getmL().size(); i++, k++) 
        {
            for (float j = m.getmL().get(i); j < m.getmL().get(k); j++, indice++) 
            {
            	res[m.getmI().get(indice)] += m.getmC().get((int) j) * vector.tab[i]; // On effectue les calculs non plus sur les colonnes de la matrice mais sur ses lignes
            	
            }
        }

		return new Vecteur(res);
	}
	*/
	//matrice transposée [ 4.0, 41.0, 45.0, 57.0]
	public Vecteur vector_transpose(Vecteur vector, Matrice m) 
	{
		float[] res = new float[vector.tab.length];
		for (int i = 1; i < m.getmL().size(); i++) 
		{
            for (float j = m.getmL().get(i - 1); j < m.getmL().get(i); j++) {
                res[m.getmI().get((int) j)] += m.getmC().get((int) j) * vector.tab[i - 1]; // On effectue les calculs non plus sur les
                //colonnes de la matrice mais sur ses lignes
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
	
	
	/*
    Fonctions de création d'une matrice à partir d'un fichier
  */

 public int matrice_size(String matrice_file) {
	 int size = -1;
	 try {
		 BufferedReader br = new BufferedReader(new FileReader(matrice_file));
		 String line;
		 while ((line = br.readLine()) != null) {
			 String[] split = line.split(" ");
			 Integer x = Integer.parseInt(split[0]);
			 Integer y = Integer.parseInt(split[1]);
			 if (x > size) {
				 size = x;
			 }
			 if (y > size) {
				 size = y;
			 }
		 }

		 br.close();
	 } catch (IOException e) {
		 System.out.println("'" + matrice_file + "' doesn't exist!");
	 }

	 return size;
 }

 public void push_matrice(Matrice m, int origin_node, LinkedList<Integer> values) 
 {
	 int size = values.size();
	 if (size == 0) {
		 return;
	 }
	 float value = 1 / (float) size;
	 for (int i = 0; i < size; i++) {
		 m.edit_value(origin_node, values.get(i), value);
	 }
 }

 public Matrice getMatriceFromFile(String filename) throws IOException {
	 Matrice m = new Matrice(this.matrice_size(filename) + 1);
	 try {
		 BufferedReader br = new BufferedReader(new FileReader(filename));
		 String line;
		 int pos = -1;
		 LinkedList<Integer> values = new LinkedList<Integer>();
		 while ((line = br.readLine()) != null) 
		 {
			 if (line.charAt(0) == '#')
					continue;
			 String[] split = line.split("[\\s\\t]");
			 Integer x = Integer.parseInt(split[0]);
			 Integer y = Integer.parseInt(split[1]);     
			 if (pos == x) {
				 values.add(y);
			 } else {
				 this.push_matrice(m, pos, values);
				 values = new LinkedList<Integer>();
				 values.add(y);
				 pos = x;
			 }
		 }
		 this.push_matrice(m, pos, values);

		 br.close();
	 } catch (IOException e) {
		 throw new IOException(e);
	 }
	 return m;
 }

 public boolean test_file(String filename) {
	 Matrice m;
	 try {
		 m = this.getMatriceFromFile(filename);
	 } catch (IOException e) {
		 System.out.println("'" + filename + "' doesn't exist.");
		 return false;
	 }

	 float[][] mat = m.getMatrice();
	 float[][] mat2 = {{0, 3, 5, 8}, {1, 0, 2, 0}, {0, 0, 0, 0}, {0, 3, 0, 0}};

	 for (int i = 0; i < mat.length; i++) {
		 for (int j = 0; j < mat[i].length; j++) {
			 if (mat2[i][j] != ((int) (10 * mat[i][j]))) {
				 System.out.println("M[" + i + "][" + j + "] = " + mat[i][j] + ", should be : " + mat2[i][j] / 10);
				 return false;
			 }
		 }
	 }
	 return true;
 }

	
	
	public static void main(String[] args) 
	{
        Integer[] L = {0, 3, 5, 5, 6};
        Float[] C = {3f, 5f, 8f, 1f, 2f, 3f};
        Integer[] I = {1, 2, 3, 0, 2, 1};
        Matrice m = new Matrice(C, L, I);


        float[] vector = {2, 5, 7, 8};
        Vecteur v = new Vecteur(vector);
        
        System.out.println("--------- Produit matrice ----------");
        System.out.println("\nMatrice CLI: " + m.toString());
        System.out.print("Vecteur : ");
        v.print_vector(v);
        System.out.print("Resultat : ");
        v.print_vector(v.produit_matrice(v, m));
        System.out.println();
		

        Matrice m1 = new Matrice(4); // version alternative d'instancier une matrice
        m1.edit_value(0, 0, 0);
        m1.edit_value(0, 1, 0);
        m1.edit_value(0, 2, 1);
        m1.edit_value(0, 3, 0);
        m1.edit_value(1, 0, 2);
        m1.edit_value(1, 1, 3);
        m1.edit_value(1, 2, 0);
        m1.edit_value(1, 3, 4);
        m1.edit_value(2, 0, 0);
        m1.edit_value(2, 1, 5);
        m1.edit_value(2, 2, 6);
        m1.edit_value(2, 3, 7);
        m1.edit_value(3, 0, 0);
        m1.edit_value(3, 1, 0);
        m1.edit_value(3, 2, 0);
        m1.edit_value(3, 3, 0);

        float[] vect = {3, 2, 7, 5};
        Vecteur v2 = new Vecteur(vect);
/*
 * Exo4 1
 * 
 * */
        System.out.println("--------- Vecteur transpose ----------");
        System.out.println("\nCLI : " + m1.toString());
        System.out.print("Vecteur : ");
        v2.print_vector(v2);
        System.out.print("Result : ");
        v2.print_vector(v2.vector_transpose(v2, m1));
        System.out.println();
    }
}
