package org.ncidence.sudoku;

import org.ncidence.sudoku.sudokusolver.SudokuSolverDriver;

public class SudokuSolverMain {
	public static void main(String[] args){
		if(args != null && args.length > 0){
			SudokuSolverDriver sudokuSolver = new SudokuSolverDriver();
			sudokuSolver.solveCsv(args[0]);
		}
	}
}
