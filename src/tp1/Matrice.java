package tp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Matrice {

	private int n;
	HashMap<Integer, HashMap<Integer, Double>> mots_pages;
	HashMap<String, Integer> numero_pages;
	private LinkedList<Float> mC;
	private LinkedList<Integer> mL;
	private LinkedList<Integer> mI;

	public Matrice (Float[] c, Integer[] l, Integer[] i) 
	{
		mC = new LinkedList<Float>(Arrays.asList(c));
		mL = new LinkedList<Integer>(Arrays.asList(l));
		mI = new LinkedList<Integer>(Arrays.asList(i));
		this.n = l.length - 1;
	}

	public  Matrice(int size)
	{
		n = size;
		mC = new LinkedList<Float>();
		mL = new LinkedList<Integer>();
		mI = new LinkedList<Integer>();
		for (int i = 0; i <= this.n; i++) {
			this.mL.add(0);
		}
	}

	/**
	 * @return the n
	 */
	 public int getN() {
		 return n;
	 }

	 /**
	  * @return the mC
	  */
	 public LinkedList<Float> getmC() {
		 return mC;
	 }

	 /**
	  * @param mC the mC to set
	  */
	 public void setmC(int n,float Mat[][]) 
	 { 
		 int i,j;
		 for(i=0;i<n;i++) 
		 { 
			 for(j=0;j<n;j++) 
			 {
				 if(Mat[i][j]!=0) 
				 {
					 mC.add(Mat[i][j]); 
				 }
			 }
		 }
	 }

	 /**
	  * @return the mL
	  */
	 public LinkedList<Integer> getmL() {
		 return mL;
	 }
	 /**
	  * @return the mI
	  */
	 public LinkedList<Integer> getmI() {
		 return mI;
	 }

	 /**
	  * @param mI the mI to set
	  */
	 public void setmI(int n, float Mat[][]) 
	 {
		 for (int i = 0; i < n; i++) {
			 for (int j = 0; j < n; j++) {
				 if (Mat[i][j] != 0) {
					 mI.add(j);
				 }
			 }
		 }
	 }

	 /**
	  * Incrémente les valeurs de mL d'index supérieurs ou égaux à index_from
	  *
	  * @param index_from
	  */

	 private void setmL(int index_from) 
	 {
		 ListIterator<Integer> it_L = mL.subList(index_from, mL.size()).listIterator();
		 int current_value;
		 while (it_L.hasNext()) {
			 current_value = it_L.next();
			 it_L.set(current_value + 1);
		 }
	 }
	 /**
	  * modifier une case de la matrice
	  *
	  * @param line
	  * @param column
	  * @param value
	  */
	 public void edit_value(int line, int column, float value) 
	 {
		 int line_start = mL.get(line), line_end = mL.get(line + 1), index = 0; //On récupère les positions de début et de fin d'une ligne à l'aide de L
		 Iterator<Integer> it = mI.subList(line_start, mI.size()).iterator();
		 boolean exist = false; //exist indique si la position de la case existe dans C

		 if (line_end - line_start > 0) //Si la ligne a au moins une valeur non vide, on cherche la case à modifier
		 { 
			 int current;
			 while (!exist && index < line_end - line_start) //tant que l'on a pas trouvé la case et que la ligne n'est pas entièrement parcourue
			 { 
				 current = it.next();

				 if (current == column) 
				 {
					 this.mC.set(line_start + index, value); //Si l'on trouve la colone recherchée dans la section de I
					 // correspondant à cette ligne, alors assigne value à la valeur correspondante dans C
					 exist = true;

				 } else if (current > column) //si on a atteint une colonne d'indice plus élevé sans la trouver, alors
					 // on insère la valeur juste avant la case courrante
				 {
					 this.mC.add(line_start + index, value);
					 this.mI.add(line_start + index, column);
					 this.setmL(line + 1); ////on modifie L après l'insertion
					 exist = true;
				 }
				 index++;
			 }
		 }
		 if (!exist) //si la ligne est vide, on insère la valeur de la case.
		 {
			 this.mC.add(line_start + index, value);
			 this.mI.add(line_start + index, column);
			 this.setmL(line + 1); //on modifie L après l'insertion
		 }
	 }

	 public float[][] getMatrice() {
		 float[][] m = new float[n][n];

		 for (int i = 0, k = 1; i < mI.size(); i++) {
			 if (i >= mL.get(k)) {
				 do {
					 k++;
				 } while (mL.get(k).equals(mL.get(k - 1)));
			 }
			 m[k - 1][mI.get(i)] = mC.get(i);
		 }
		 return m;
	 }

	 @Override
	 public String toString() 
	 {
		 return "Matrice{" +
				 "C=" + mC +
				 ", L=" + mL +
				 ", I=" + mI +
				 ", n=" + n +
				 '}';
	 }
	 
	 public static void main(String[] args) 
	 {
		 Integer[] L = {0, 3, 5, 5, 6};
		 Float[] C = {3f, 5f, 8f, 1f, 2f, 3f};
		 Integer[] I = {1, 2, 3, 0, 2, 1};
		 Matrice m = new Matrice(C, I, L);
		 System.out.println("\nMatrice : " + m.toString());
		 /*
		        float[][] tab = m.getMatrice();
		        for(int i = 0; i < tab.length; i++)
		        {
		        	for(int j = 0; j < tab.length; j++)
			        {
			        	System.out.println(" "+tab[i][j]);
			        }
		        }
		  */
	 }
}
