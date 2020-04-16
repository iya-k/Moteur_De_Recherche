package tp1;

import java.io.IOException;
import java.util.Arrays;

public class PageRank 
{
	Matrice m;
	Vecteur v;

	public PageRank(Vecteur vect, Matrice mat) 
	{
		v = vect;
		m = mat;
    }

    /**
     *  Retourne la liste des probabilités d'être sur chaque sommet en partant de la liste de probabilité p
     * @param m matrice des probabilités
     * @param p
     * @param nb_pas
     * @return
     */
    public Vecteur page_rank(float[] p, int nb_pas)
    {
    	Vecteur result = new Vecteur(p.clone());
        for (int i = 0; i < nb_pas; i++)
        {
             result = v.vector_transpose(result, m);
            v.print_vector(result);
        }
        return result;
    }

    public Vecteur page_rank_from_sommet(int nb_pas, int num_sommet)
    {
        float[] p=new float[v.getN()];
        Arrays.fill(p,0);
        p[num_sommet]=1;
        return this.page_rank(p,nb_pas);
    }

    public Vecteur page_rank_zero(int nb_pas)
    {
        return this.page_rank_from_sommet(nb_pas,0);
    }

     /**
      * Retourne la liste des probabilités d'être sur chaque sommet en partant de la liste de probabilité p
      * pour l'algorithme zap
     * @param m matrice des probabilités
     * @param p
     * @param nb_pas
     * @return
     */
    public Vecteur page_rank_zap(float[] p, int nb_pas, float zap)
    {
    	Vecteur result = new Vecteur(p.clone());
        for (int i = 0; i < nb_pas; i++){
            result = v.vector_transpose_zap(result, zap);
            v.print_vector(result);;
        }
        return result;
    }

    public Vecteur page_rank_from_sommet_zap(int nb_pas, int num_sommet, float zap)
    {
        float[] p=new float[v.getN()];
        Arrays.fill(p,0);
        p[num_sommet]=1;
        return this.page_rank_zap(p,nb_pas,zap);
    }

    public Vecteur page_rank_zero_zap(int nb_pas, float zap)
    {
        return this.page_rank_from_sommet_zap(nb_pas,0, zap);
    }
    
    public static void main(String[] args)
    {
    	Matrice[] m = new Matrice[5];
        
        float[] vect = {3, 2, 7, 5};

        Vecteur v = new Vecteur(vect);
        
        try {
            m[0] = v.getMatriceFromFile("./resources/exemple.txt");
            m[1] = v.getMatriceFromFile("./resources/exemple5.txt");
            m[2] = v.getMatriceFromFile("./resources/gr1.txt");
            m[3] = v.getMatriceFromFile("./resources/facebookMini.txt");
            m[4] = v.getMatriceFromFile("./resources/fb2.txt");
        } catch (IOException e) 
        {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("page rank zero");
        int i = 0;
        /*for (Matrice matrix : m) {

            PageRank r = new PageRank(v, matrix);
            System.out.println("\nMatrice " +i+": "+ matrix.toString());
            System.out.print("From 0: ");
            r.page_rank_zero(100);
            i++;
        }
        */
        System.out.println("page rank zero zap");
        for (Matrice mat : m) {
        	PageRank r = new PageRank(v, mat);
            System.out.println("\nMatrice : " + mat.toString());
            System.out.print("From 0 : ");
            r.page_rank_zero_zap(10, 1.5f);
        }
    }

}
