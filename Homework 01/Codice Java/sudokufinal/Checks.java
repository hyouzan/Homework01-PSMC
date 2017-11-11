/*
 * Classe che contiene tutti i vari controlli per verificare se un valore è valido in una cella
 * 
*/

package sudokufinal;

public class Checks {

	
	public static boolean isLegal(int i, int r, int c, int[][] matrix) 
	{
		return checkRows(i, c, matrix) && checkColumns(i, r, matrix) && checkSquare(i, c, r, matrix);
	}
	
	public static boolean checkSquare(int i, int c, int r, int[][] matr) 
	{
		
		int boxRowOffset = (r / 3)*3;
        int boxColOffset = (c / 3)*3;
        
        for (int m = 0; m < 3; ++m) // box
            for (int n = 0; n < 3; ++n)
                if (i == matr[boxRowOffset+m][boxColOffset+n])
                    return false;

        return true;
	}

	public static boolean checkColumns(int i, int r, int[][] matr) 
	{
		for(int j = 0; j < Constants.MATR_SIZE; j++)
		{
			int x = matr[r][j];
			
			if(x == i)
				return false;
			
		}
		
		return true;
	}

	public static boolean checkRows(int i, int c, int[][] matr) 
	{
		for(int j = 0; j < Constants.MATR_SIZE; j++)
		{
			int x = matr[j][c];
			
			if(x == i)
				return false;
			
		}
		
		return true;
	}
}
