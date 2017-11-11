/*
 * Gestione della griglia sudoku, fillGrid prende l'input e riempe la griglia: sia con i valori già fissati che con le soluzioni (addPossibleSolutions)
 * 
 */

package sudokufinal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SudokuGrid {

	private int[][] sudokugrid = new int[Constants.MATR_SIZE][Constants.MATR_SIZE];
	private int emptyints =0; 
	
	public void fillGrid() throws FileNotFoundException
	{
		Scanner in = new Scanner(new File(Constants.fileInput));
		int row = 0;
		
		while(in.hasNext())
		{
			String line = in.nextLine();
			String[] array = line.split("");
			
			for(int col = 0; col < array.length; col++)
			{
				if(array[col].equals(".") )
				{
					sudokugrid[row][col] = Constants.NULL;
					emptyints++;
				}
				else
				{
					sudokugrid[row][col] = Integer.parseInt(array[col]);
				}
			}
			
			row++;
		}
		in.close();
		
		addPossibleSolutions();
	}

	public int getEmptyints() {
		return emptyints;
	}

	private void addPossibleSolutions() 
	{
		for(int r=0;r<sudokugrid.length;r++)
		{
			for(int c=0;c<sudokugrid .length;c++)
			{
				int x = getint(r, c);
				
				if(x == Constants.NULL)
				{
					List<Integer> sol = new ArrayList<Integer>();
					for(int i=1;i<10;i++)
					{
						if(Checks.isLegal(i,r,c,sudokugrid ))
						{
							sol.add(i);
						}
					}
					
					if(sol.size() == 1)
					{
						sudokugrid[r][c] = sol.get(0);
						r = 0;
						c = 0;
					}
				}
			}
		}
	}
	

	public int[][] getGrid() {
		return sudokugrid;
	}

	public int getint(int r, int c) 
	{
		return sudokugrid[r][c];
	}
}
