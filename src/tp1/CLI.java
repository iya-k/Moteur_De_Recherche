package tp1;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class CLI 
{
	private int n;
	private LinkedList<Float> mC;
	private LinkedList<Integer> mL;
	private LinkedList<Integer> mI;



	public CLI (Matrice m) 
	{
		n = m.getMatrice().length;
		mC = new LinkedList<Float>();
		mL = new LinkedList<Integer>();
		mI = new LinkedList<Integer>();
		
		int i,j;
		for(i=0;i<n;i++) 
		{ 
			for(j=0;j<n;j++) 
			{
				if(m.getMatrice()[i][j]!=0) 
				{
					//System.out.println(m.getMatrice()[i][j]);
					mC.add(m.getMatrice()[i][j]); 
					mI.add(j);
				}
			}
		}
		
		int count; 
		mL.add(0);
		for(i=0;i<n;i++) 
		{ 
			count=0; 
			for(j=0;j<n;j++) 
			{ 
				if(m.getMatrice()[i][j]!=0) 
				{
					count+=1;
				}
			} 
			if(count==0) 
			{ 
				mL.add(mL.get(mL.size()-1)); 
			}
			else 
			{
				mL.add(count+mL.get(mL.size()-1)); 
			}
		}
		
	}

	public CLI (Float[] c, Integer[] l, Integer[] i) 
	{
		mC = new LinkedList<Float>(Arrays.asList(c));
		mL = new LinkedList<Integer>(Arrays.asList(l));
		mI = new LinkedList<Integer>(Arrays.asList(i));
		this.n = l.length - 1;
	}
	
	public  CLI(int size)
	{
		n = size;
		mC = new LinkedList<Float>();
		mL = new LinkedList<Integer>();
		mI = new LinkedList<Integer>();
		for (int i = 0; i <= this.n; i++) 
		{
			this.mL.add(0);
		}
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
	public void setmC(LinkedList<Float> mC) 
	{
		this.mC = mC;
	}

	/**
	 * @return the mL
	 */
	public LinkedList<Integer> getmL() {
		return mL;
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
	 * @return the mI
	 */
	public LinkedList<Integer> getmI() {
		return mI;
	}

	/**
	 * @param mI the mI to set
	 */
	public void setmI(LinkedList<Integer> mI) {
		this.mI = mI;
	}

	 /**
	  * A enlever car j'utilise une matrice pour creer la matrice CLI
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

	/**
	 * @return the n
	 */
	public int getN() {
		return n;
	}

	public void setI(int n, float Mat[][]) 
	{
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (Mat[i][j] != 0) {
					mI.add(j);
				}
			}
		}
	}
 
	public int maxI() {
		int MAX = 0;
		for (int k = 0; k < mI.size(); k++) {
			if (mI.get(k) > MAX)
				MAX = mI.get(k);
		}
		return MAX;
	}
	
	 @Override
	 public String toString() 
	 {
		 return "CLI {" +
				 "C=" + mC +
				 ", L=" + mL +
				 ", I=" + mI +
				 ", n=" + n +
				 '}';
	 }
	
	 public static void main(String[] args) 
	 {
		 Float[] C = {3f, 5f, 8f, 1f, 2f, 3f};
		 Integer[] L = {0, 3, 5, 5, 6};
		 Integer[] I = {1, 2, 3, 0, 2, 1};
		 CLI c = new CLI(C, L, I);
		 System.out.println("\nMatrice : " + c.toString());

		 //Matrice : CLI {C=[3.0, 5.0, 8.0, 1.0, 2.0, 3.0], L=[0, 3, 5, 5, 6], I=[1, 2, 3, 0, 2, 1], n=5}
		
		  Matrice m1 = new Matrice(4); // version alternative d'instancier une matrice
	        m1.edit_value(0, 1, 3);
	        m1.edit_value(0, 2, 5);
	        m1.edit_value(0, 3, 8);
	        m1.edit_value(1, 0, 1);
	        m1.edit_value(1, 2, 2);
	        m1.edit_value(3, 1, 3);

			 System.out.println("\nMatrice : " + new CLI(m1).toString());
			 //Matrice : CLI {C=[3.0, 5.0, 8.0, 1.0, 2.0, 3.0], L=[0, 3, 5, 5, 6], I=[1, 2, 3, 0, 2, 1], n=4}

	 }
}
