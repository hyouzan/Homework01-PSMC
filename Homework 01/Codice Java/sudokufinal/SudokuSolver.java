package sudokufinal;

import java.util.ArrayList;
import java.util.List;

public class SudokuSolver {


	public static List<ArrayList<Integer>> trySolutions(int[][] initialMatr, List<ArrayList<Integer>> sudokulists, int row, int col) 
	{
		List<ArrayList<Integer>> newSudokulists = new ArrayList<ArrayList<Integer>>();
		
		for(List<Integer> array: sudokulists)
		{
			//mi creo un'altra matrice uguale a quella iniziale per fare i controlli sulle soluzioni
			//devo trasformare l'array dei numeri in matrice con "buildMatr" per problemi di overhead memoria
			int[][] matr = buildMatr(createNewMatr(initialMatr), array);
			
			for(int i = 1; i <= Constants.MATR_SIZE; i++)
			{
				//è soluzione?
				if(Checks.isLegal(i, row, col, matr))
				{
					//e' soluzione, lo aggiungo al nuovo cammino (nuovo ramo dell'albero)
					ArrayList<Integer> arrayDaAgg = new ArrayList<Integer>();
					
					arrayDaAgg.addAll(array);
					arrayDaAgg.add(i);
					
					newSudokulists.add(arrayDaAgg);
				}
			}
		}
		
		return newSudokulists;
	}
	
	public static int[][] buildMatr(int[][] matr, List<Integer> array) 
	{
		int i = 0;
		
		for(int r = 0; r < Constants.MATR_SIZE; r++)
		{
			for(int c = 0; c < Constants.MATR_SIZE; c++)
			{
				if(matr[r][c] == Constants.NULL)
				{
					if(i < array.size())
					{
						matr[r][c] = array.get(i);
						i++;
					}
				}
				else
					matr[r][c] = matr[r][c];
			}
		}
		
		return matr;
	}
	

	public static int[][] createNewMatr(int[][] matr)
	{
		int[][] newMatr = new int[Constants.MATR_SIZE][Constants.MATR_SIZE];
		
		for(int i = 0; i < matr.length; i++)
		{
			  int[] aMatrix = matr[i];
			  int   aLength = aMatrix.length;
			  newMatr[i] = new int[aLength];
			  System.arraycopy(aMatrix, 0, newMatr[i], 0, aLength);
		}
		
		return newMatr;
	}

	public static List<ArrayList<Integer>> bruteforce(int i, int j, List<ArrayList<Integer>> sudokulists, int[][] initialMatr, boolean first) 
	{
		/* se e' la prima parte del programma
		 * faccio fino alla terza riga (variabile) TUTTE le possibili matrici corrette
		 * altrimenti
		 * lo faccio dalla terza riga fino alla fine
		 */
		for(int r = i; r < ((first) ? Constants.RIGHE_INIZIO : Constants.MATR_SIZE); r++)
		{
			for(int c = j; c < Constants.MATR_SIZE; c++)
			{
				int cell = initialMatr[r][c];
				
				if(cell == Constants.NULL)
				{
					//provo altri eventuali "rami" dell'albero
					sudokulists = trySolutions(initialMatr, sudokulists, r, c);
					
				}
			}
		}
		
		return sudokulists;
		
	}

	public static ArrayList<SudokuThread> parallelCheck(int[][] matr, int row, int col) 
	{
		int[][] newMatr = createNewMatr(matr);
		
		ArrayList<SudokuThread> listThread = new ArrayList<SudokuThread>();
		
		for(int i = 1; i <= Constants.MATR_SIZE; i++)
		{
			if(Checks.isLegal(i, row, col, newMatr))
			{
				newMatr[row][col] = i;
				
				SudokuThread thread = new SudokuThread(newMatr, row, col);
				
				listThread.add(thread);
				
				thread.start();
			}
		}
		
		return listThread;
	}

}
