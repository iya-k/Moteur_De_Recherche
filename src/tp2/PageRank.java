package tp2;

import java.io.IOException;

import tp1.CLI;
import tp1.Matrice;
import tp1.Vecteur;
import utils.Commons;

public class PageRank extends Commons
{
	//difference de deux vecteurs
    public double diff(Vecteur v1, Vecteur v2)
    {
    	if(v1.getTab().length != v1.getTab().length)
    	{
    		System.out.println("taille non conforme");
    	}
		double d = 0.0;
	    for(int i = 0; i < v1.getTab().length; i++){
	        d += Math.abs(v1.getTab()[i] - v2.getTab()[i]);
	    }
	    return d;
	}
    //page_rank avec le facteur epsilon
	 public Vecteur page_rank_zero_epsilon(CLI cli, Vecteur Z, double ep)
	    {
		 double sigma = 0;
		 Vecteur p0 = Z;
	        do
	        {
	        	Vecteur p1 = vector_transpose(p0, cli);
	            System.out.println(p0.toString());
	            sigma = diff(p0,p1);
	            p0 = p1;
	       
	        }while( sigma > ep);
	        
	        return p0;
	    }

    public Vecteur page_rank_from_sommet(CLI cli, Vecteur Z, int nb_pas, int num_sommet)
    {
    	/*
        float[] p=new float[cli.getN()];
        Arrays.fill(p,0);
        p[num_sommet]=1;
        */
        Vecteur result = Z;
        for (int i = 0; i < nb_pas; i++)
        {
             result = vector_transpose(result, cli);
             System.out.println(result.toString());
        }
        return result;
    }

    public Vecteur page_rank_zero(CLI cli, Vecteur Z, int nb_pas)
    {
        return page_rank_from_sommet(cli, Z, nb_pas,0);
    }

    /**
     * Retourne la liste des probabilités d'être sur chaque sommet en partant de la liste de probabilité p
     * pour l'algorithme zap
     * @param cli matrice,
     * @param Z liste des probabilités
     * @param p
     * @param nb_pas
     * @return
     */
    public Vecteur page_rank_from_sommet_zap(CLI cli, Vecteur Z, int nb_pas, int num_sommet, float zap)
    {
        //float[] p = new float[cli.getN()];
        //Arrays.fill(p,0);
        //p[num_sommet] = 1;
        
        Vecteur result = Z;//new Vecteur(p.clone());
        for (int i = 0; i < nb_pas; i++){
            result = vector_transpose_zap(result, zap);
            System.out.println(result.toString());
        }
        return result;
    }

    public Vecteur page_rank_zero_zap(CLI cli, Vecteur Z, int nb_pas, float zap)
    {
        return page_rank_from_sommet_zap(cli, Z, nb_pas,0, zap);
    }
    public Vecteur page_rank_zero_zap_epsilon(CLI cli, Vecteur Z, float zap, double ep)
    {
	 double sigma = 0;
	 Vecteur p0 = Z;
        do
        {
        	Vecteur p1 = vector_transpose_zap(p0, zap);
            System.out.println(p0.toString());
            sigma = diff(p0,p1);
            p0 = p1;
       
        }while( sigma > ep);
        
        return p0;
    }
    
  //produit de la matrice
  	public static Vecteur produit_matrice(Vecteur vector, CLI m) 
  	{
  		if( m.getmL().size() - 1 != vector.getN())
  		{
  			System.out.println("erreur dimension");
  			return null;
  		}
  		float[] res = new float[vector.getN()];
  		
  		for (int i = 0, k = 1; k < m.getmL().size(); i++, k++) 
  		{
  			for (float j = m.getmL().get(i); j < m.getmL().get(k); j++)
  			{	
  				//System.out.println("indice vector = "+ m.getmI().get((int) j));
  				res[i] +=  m.getmC().get((int) j) * vector.getTab()[m.getmI().get((int) j)];
  				//System.out.println(res[i] );
  			}
  		}
  		return new Vecteur(res);

  	}
  	//matrice transposée [ 4.0, 41.0, 45.0, 57.0]
  	public static Vecteur vector_transpose(Vecteur vector, CLI m) 
  	{
  		
  		if( m.getmL().size() - 1 != vector.getN())
  		{
  			System.out.println("erreur dimension");
  			return null;
  		}
  		float[] res = new float[vector.getN()];
  		
  		int n = m.getmL().size() - 1;

  	    int nb_aleat = n / 300;
  	    if (nb_aleat == 0)
  	    {
  	        nb_aleat = 1;
  	    }
  	    double delta = 1 / nb_aleat;
  	  
  		for (int i = 1; i < m.getmL().size(); i++) 
  		{
  			if (m.getmL().get(i - 1) == m.getmL().get(i)) {
  	            int aleat = (int) (Math.random() * nb_aleat);
  	            for (int a = 0; a < aleat; a++)
  	            {
  	            	res[m.getmI().get((int) a)] += delta * vector.getTab()[i - 1];
  	            }
  			}else {
              for (float j = m.getmL().get(i - 1); j < m.getmL().get(i); j++) {
                  res[m.getmI().get((int) j)] += m.getmC().get((int) j) * vector.getTab()[i - 1]; // On effectue les calculs non plus sur les
                  //colonnes de la matrice mais sur ses lignes
              }
  			}
          }

  		return new Vecteur(res);
  	}
  	// après avoir calculé le produit par la transposée, on modifie chaque valeur du vecteur afin de prendre en compte le facteur de zap
  	public Vecteur vector_transpose_zap(Vecteur vector, float zap) {
  		float[] result = vector.getTab();

          for (int i = 0; i < result.length; i++) 
          {
          	result[i] *= (1 - zap) + (zap / vector.getN());// On effectue les calculs non plus sur les colonnes de la matrice mais sur ses lignes
          }

          return new Vecteur(result);
      }
  	
    public static void main(String[] args)
    {   
    	Integer[] L = {0, 3, 5, 5, 6};
        Float[] C = {3f, 5f, 8f, 1f, 2f, 3f};
        Integer[] I = {1, 2, 3, 0, 2, 1};
        CLI m = new CLI(C, L, I);


        float[] vector = {2, 5, 7, 8};
        Vecteur v = new Vecteur(vector);
        
        System.out.println("--------- Produit matrice ----------");
        System.out.println("\nMatrice CLI: " + m.toString());
        System.out.print("\n");
        System.out.println(v.toString());
        System.out.print("Resultat : ");
        System.out.println(produit_matrice(v, m).toString());

        float[] vect = {3, 2, 7, 5};
        Vecteur v2 = new Vecteur(vect);
        
        Matrice mat = new Matrice(4); // version alternative d'instancier une matrice
        mat.edit_value(0, 1, 3);
        mat.edit_value(0, 2, 5);
        mat.edit_value(0, 3, 8);
        mat.edit_value(1, 0, 1);
        mat.edit_value(1, 2, 2);
        mat.edit_value(3, 1, 3);
        
        CLI cli = new CLI(mat);
        System.out.println("\n\n"+mat.toString()+ "\n"+ cli.toString());
        PageRank pg = new PageRank();
        System.out.println("PageRank epsilon");
        pg.page_rank_zero_epsilon(cli, v, EPSILON);
        
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
        CLI c1 = new CLI(m1);
        
/*
 * Exo4 1
 * 
 * */
        System.out.println("\n--------- Vecteur transpose ----------");
        System.out.println("\n " + c1.toString());
        System.out.print("\n");
        System.out.println(v2.toString());
        System.out.print("Result : ");
        System.out.println((vector_transpose(v2, c1).toString()));
        System.out.println();
        
        try {
        	CLI[] cl = new CLI[2];
        	cl[0] = getMatriceFromFile("./resources/exemple.txt");
        	cl[1] = c1;
            PageRank r;
            System.out.println("-------------- page rank zero");
            int i = 0;
            for (CLI c : cl) 
            {
                r = new PageRank();
                System.out.println("\nMatrice " +i+": "+ c.toString());
                System.out.print("From 0:\n");
                r.page_rank_zero(c,v2, 10);
                i++;
            }
            System.out.println("\n\npage rank zero zap");
            for (CLI c : cl) {
            	r = new PageRank();
                System.out.println("\nMatrice : " + c.toString());
                System.out.print("From 0 : ");
                r.page_rank_zero_zap(c,v2, 10, ZAP_D);
            } 
            
        } catch (IOException e) 
        {
            System.out.println(e.getMessage());
            return;
        }
    }

}
