package tp1;

public class Matrice {

	private int n;
	private float[][] mat;

	public Matrice (float[][] m) 
	{
		mat = m;
	}

	public  Matrice(int size)
	{
		n = size;
		mat = new float[n][n];

		int i,j;
		for(i=0;i<n;i++) 
		{ 
			for(j=0;j<n;j++) 
			{
				mat[i][j] = 0; 
			}
		}
	}

	public float[][] getMatrice() 
	{
		return mat;
	}

	@Override
	public String toString() 
	{
		String matrice = "";
		for(int i = 0; i < n; i++)
		{
			String s = "[";
			for(int j = 0; j < n; j++)
			{
				s += mat[i][j]+",";
			}
			matrice += s+"]"+"\n";
			s = "";
			//System.out.println(matrice);
		}

		return "Matrice:{\n" +
		matrice +
		'}';
	}
	public void edit_value(int line, int column, float value) 
	 {
		 if(line < n && column < n)
		 {
			 mat[line][column] = value;
		 }
	 }
	public static void main(String[] args) 
	{
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
		System.out.println("\n " + m1.toString());
		
	}
}
