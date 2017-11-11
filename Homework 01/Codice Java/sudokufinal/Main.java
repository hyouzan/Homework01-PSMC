package sudokufinal;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	private static double speedUp = 0; 

	public static void main(String[] args) throws FileNotFoundException 
	{
		
		SudokuGrid init = new SudokuGrid();
		init.fillGrid();
		int celle_vuote = init.getEmptyints();
		System.out.println("Celle vuote: "+celle_vuote);
		
		int fat_riemp = (int) (new Double(celle_vuote) /  new Double(Constants.TOT_CELLS) * 100);
		
		
		System.out.println("Fattore riempimento: "+(100 - fat_riemp)+"%\n");
		System.out.println("INIZIO SEQUENZIALE \n");
		
		sequenziale(init);
		
		//PARALLELO ------------------------------------------------------------------------------------------
		
		
		System.out.println("\nINIZIO PARALLELO\n");
		
		parallelo(init);
		
		System.out.println("\nspeedUp: "+speedUp);
		
		
		
	}
	
	private static void parallelo(SudokuGrid init) 
	{
		List<SudokuThread> tuttiThread = new ArrayList<SudokuThread>();
		List<ArrayList<Integer>> sudokulists = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> hello = new ArrayList<ArrayList<Integer>>();
		
		// riempio l'array con 1 elemento altrimenti -> NULL exception
		hello.add(new ArrayList<Integer>());
		
		//radice albero
		sudokulists.addAll(hello);
		
		int[][] initialMatr = init.getGrid();
		
		//tempo di partenza
		long now = System.currentTimeMillis();
		
		sudokulists = SudokuSolver.bruteforce(0, 0, sudokulists, initialMatr, true);
		
		int countSol = 0;
		
		List<ArrayList<Integer>> newSudokulists = new ArrayList<ArrayList<Integer>>();
		newSudokulists.add(new ArrayList<Integer>());
		
		
		System.out.println("num thread: "+sudokulists.size());
		
		//ho completato l'albero fino alla terza righa per colpa della memoria
		for(ArrayList<Integer> array: sudokulists)
		{
			newSudokulists.set(0, array);
			
			SudokuThread t = new SudokuThread(SudokuSolver.buildMatr(SudokuSolver.createNewMatr(initialMatr), array), Constants.RIGHE_INIZIO, 0);
			
			tuttiThread.add(t);
			
			t.start();
		}
		
		for(SudokuThread t: tuttiThread)
		{
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			countSol += t.getCount();
		}
		
		long finish = System.currentTimeMillis();
		
		System.out.println("TEMPO IMPIEGATO: "+((finish - now) / 1000.0)+" sec");

		System.out.println("SOLUZIONI TROVATE: "+countSol);
		
		speedUp = speedUp / ((finish - now) / 1000.0);
	}

	private static void sequenziale(SudokuGrid init) 
	{
		List<ArrayList<Integer>> sudokulists = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> hello = new ArrayList<ArrayList<Integer>>();
		
		// riempio l'array con 1 elemento altrimenti -> NULL exception
		hello.add(new ArrayList<Integer>());
		
		sudokulists.addAll(hello);
		
		int[][] initialMatr = init.getGrid();
		
		//tempo di partenza
		long now = System.currentTimeMillis();
		
		/*
		 * questo mi genererà poi il numero di thread da usare
		 * inizio il metodo con la matrice iniziale
		 * dopo sara' chiamato l' "alberone"
		 * 
		 */
		
		sudokulists = SudokuSolver.bruteforce(0, 0, sudokulists, initialMatr, true);
		
		int countSol = 0;
		
		List<ArrayList<Integer>> newSudokulists = new ArrayList<ArrayList<Integer>>();
		newSudokulists.add(new ArrayList<Integer>());
		
		//ho un albero di possibili soluzioni corrette fino all'inizio della riga 3
		
		for(ArrayList<Integer> array: sudokulists)
		{
			/*
			 * qui riuso ogni nodo dell'alberone come radice
			 * rifaccio girare l'algoritmo su ogni nodo come se fosse radice
			 * queste "radici" le userò come threads del parallelo
			 */
			newSudokulists.set(0, array);
			
			countSol += SudokuSolver.bruteforce(Constants.RIGHE_INIZIO, 0, newSudokulists, initialMatr, false).size();
		}
		
		long finish = System.currentTimeMillis();
		
		
		System.out.println("TEMPO IMPIEGATO: "+((finish - now) / 1000.0)+" sec");
		
		System.out.println("SOLUZIONI TROVATE: "+countSol);
		
		speedUp = ((finish - now) / 1000.0);
		
	}

	public static void printMatrix(int[][] matr) 
	{
		for(int i = 0; i < matr.length; i++)
		{
			for(int j = 0; j < matr.length; j++)
			{
				System.out.print(matr[i][j]+" ");
			}
			System.out.println();
		}
		
		System.out.println("----------------");
	}
}
