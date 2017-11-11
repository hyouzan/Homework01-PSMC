package sudokufinal;

import java.util.ArrayList;
import java.util.List;

public class SudokuThread extends Thread {

	private int[][] matr;
	private int row;
	private int col;
	
	private int countSoluzioni = 0;

	public SudokuThread(int[][] matr, int row, int col) 
	{
		this.matr = matr;
		this.row = row;
		this.col = col;
	}
	
	
	public void run()
	{
		
		List<ArrayList<Integer>> sudokulists = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> hello = new ArrayList<ArrayList<Integer>>();
		
		hello.add(new ArrayList<Integer>());
		
		sudokulists.addAll(hello);
		
		countSoluzioni += SudokuSolver.bruteforce(row, col, sudokulists, matr, false).size();
	}


	public int getCount() 
	{
		return countSoluzioni;
	}
	
	
}
