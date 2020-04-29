package tp1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CLI 
{
	private int n;
    HashMap<Integer, HashMap<Integer, Double>> mots_pages;
    HashMap<String, Integer> numero_pages;
    private String lastTitle;
	private ArrayList<Float> mC;
	private ArrayList<Integer> mL;
	private ArrayList<Integer> mI;



	public CLI () 
	{
		mC = new ArrayList<Float>();
		mL = new ArrayList<Integer>();
		mI = new ArrayList<Integer>();
	}

	public CLI (ArrayList<Float> c, ArrayList<Integer> l, ArrayList<Integer> i) 
	{
		mC = c;
		mL = l;
		mI = i;
	}
	
	public  CLI(int size)
	{
		n = size;
		
		mC = new ArrayList<Float>();
		mL = new ArrayList<Integer>();
		mI = new ArrayList<Integer>();
		for (int i = 0; i <= this.n; i++) {
            this.mL.add(0);
        }
	}

	/**
	 * @return the mC
	 */
	public ArrayList<Float> getmC() {
		return mC;
	}

	/**
	 * @param mC the mC to set
	 */
	public void setmC(ArrayList<Float> mC) 
	{
		this.mC = mC;
	}

	/**
	 * @return the mL
	 */
	public ArrayList<Integer> getmL() {
		return mL;
	}

	/**
	 * @param mL the mL to set
	 */
	public void setmL(ArrayList<Integer> mL) {
		this.mL = mL;
	}

	/**
	 * @return the mI
	 */
	public ArrayList<Integer> getmI() {
		return mI;
	}

	/**
	 * @param mI the mI to set
	 */
	public void setmI(ArrayList<Integer> mI) {
		this.mI = mI;
	}


	public void setC(int n,float Mat[][]) 
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
     * Incrémente les valeurs de L d'index supérieurs ou égaux à index_from
     *
     * @param index_from
     */
    /*private void modify_L(int index_from) {

        ListIterator<Integer> it_L = L.subList(index_from, L.size()).listIterator();
        int current_value;
        while (it_L.hasNext()) {
            current_value = it_L.next();
            it_L.set(current_value + 1);
        }
    }
*/
	public void setL(int n , float Mat[][]) 
	{ 
		int count; 
		for(int i=0;i<n;i++) 
		{ 
			count=0; 
			for(int j=0;j<n;j++) 
			{ 
				if(Mat[i][j]!=0) 
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

	public void setI(int n, float Mat[][]) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (Mat[i][j] != 0) {
					mI.add(j);
				}
			}
		}
	}

    @Override
    public String toString() {
        return "Matrice{" +
                "C=" + mC +
                ", L=" + mL +
                ", I=" + mI +
                ", n=" + n +
                '}';
    }
    
	void afficheCLI() {
		for (int k = 0; k < mC.size(); k++) {
			System.out.print(mC.get(k) + "\t");
		}

		System.out.print("\n");
		for (int l = 0; l < mL.size(); l++) {
			System.out.print(mL.get(l) + "\t");
		}
		System.out.print("\n");
		for (int k = 0; k < mI.size(); k++) {
			System.out.print(mI.get(k) + "\t");
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

	/*
	 * 
	 * public float[][] getMatrice() {
        float[][] mat = new float[n][n];

        for (int i = 0, k = 1; i < I.size(); i++) {
            if (i >= L.get(k)) {
                do {
                    k++;
                } while (L.get(k).equals(L.get(k - 1)));
            }
            mat[k - 1][I.get(i)] = C.get(i);
        }
        return mat;
    }

    public void print_float_table(float[] vecteur) {
        String str = "";
        for (int i = 0; i < vecteur.length; i++) {
            str += vecteur[i];
            str += (i == vecteur.length - 1) ? "" : ", ";
        }
        System.out.println("[ " + str + "]");
    }

	public float[] mult_vect(float[] vecteur) {
        float[] result = new float[n];
        for (int i = 1; i < L.size(); i++) {
            for (float j = L.get(i - 1); j < L.get(i); j++) {
                result[i - 1] += C.get((int) j) * vecteur[I.get((int) j)];
            }
        }
        return result;
    }
	 * 
	 */
	/*
	public ArrayList<Float> matriceVector(ArrayList<Float> vect) {

		ArrayList<Float> res = new ArrayList<Float>(mC.size());
		float local = 0;
		for (int i = 0; i < vect.size(); i++) {
			res.set(i, (float) 0);

			for (int j = mL.get(i); j <mL.get(i + 1); j++) {
				local = local + mC.get(j) * vect.get(mI.get(j));
				res.set(i, local);
			}
		}
		return res;

	}
	*/
	//produit de la matrice
	public List<Float> produitMatrice(ArrayList<Float> vector) {

		List<Float> res = new ArrayList<Float>();
		int n = maxI() + 1;
		
		for (int i = 0; i < n; i++) 
		{
			res.add(i, 0.f);
		}

		for (int i = 0; i < n; i++) 
		{
			for (int j = mL.get(i); j < mL.get(i + 1); j++)
			{
				float index = res.get(mI.get(j)) + (mC.get(j) * vector.get(i));
				res.set(mI.get(j), index);
			}
		}
		return res;

	}
	
/*
 * 
 * 
 *   public float[] mult_vect_transp(float[] vecteur) {
        float[] result = new float[n];

        for (int i = 1; i < L.size(); i++) 
        {
            for (float j = L.get(i - 1); j < L.get(i); j++) 
            {
                result[I.get((int) j)] += C.get((int) j) * vecteur[i - 1]; // On effectue les calculs non plus sur les
                //colonnes de la matrice mais sur ses lignes
            }
        }
        return result;
    }
 * 
 */
	//matrice transposée
	public List<Float> vector_transpose(ArrayList<Float> vector) 
	{
		ArrayList<Float> res = new ArrayList<Float>(vector.size());
		for (int i = 1; i < mL.size(); i++) 
        {
            for (float j = mL.get(i - 1); j < mL.get(i); j++) 
            {
            	float value = res.get(mI.get((int) j));
            	value += mC.get((int) j) * vector.get(i - 1); // On effectue les calculs non plus sur les colonnes de la matrice mais sur ses lignes
            	res.set(mI.get((int) j), value);
            }
        }

		return res;
	}

	// après avoir calculé le produit par la transposée, on modifie chaque valeur du vecteur afin de prendre en compte le facteur de zap
	public List<Float> vector_transpose_zap(ArrayList<Float> vector, float zap) {
		List<Float> result = this.vector_transpose(vector);

        for (int i = 0; i < result.size(); i++) 
        {
        	float index = result.get(i) * (1 - zap) + (zap / this.n);// On effectue les calculs non plus sur les colonnes de la matrice mais sur ses lignes
        	result.set(i, index);
        }

        return result;
    }
}
