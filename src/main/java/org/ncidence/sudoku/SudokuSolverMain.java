package org.ncidence.sudoku;

import org.ncidence.sudoku.sudokusolver.SudokuSolverTailRecursionDriver;

public class SudokuSolverMain {
	public static void main(String[] args){
		if(args != null && args.length > 0){
			SudokuSolverTailRecursionDriver sudokuSolver = new SudokuSolverTailRecursionDriver();
			sudokuSolver.solveCsv(args[0]);
		}
	}
}
