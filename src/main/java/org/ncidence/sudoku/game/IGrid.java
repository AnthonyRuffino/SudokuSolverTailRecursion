package org.ncidence.sudoku.game;

import org.ncidence.sudoku.sudokusolver.SudokuSolverFatalException;

public interface IGrid {
	void init(String[] cellsAsStrings) throws SudokuSolverFatalException;
	void init(IGrid predecessor) throws SudokuSolverFatalException;
	Cell[] getCells();
	IGrid getLastHopefulPredecessor();
	boolean isHopeful();
	Integer[] getSolution() throws SudokuSolverFatalException;
}
