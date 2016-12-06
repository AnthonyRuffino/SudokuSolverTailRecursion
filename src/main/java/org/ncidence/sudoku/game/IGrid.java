package org.ncidence.sudoku.game;

import org.ncidence.sudoku.sudokusolver.SudokuSolverFatalException;

public interface IGrid {
	void init(String[] cellsAsStrings) throws SudokuSolverFatalException;
	void init(IGrid predecessor) throws SudokuSolverFatalException;
	Cell[] getCells();
	
	IGrid getLastHopefulPredecessor();
	boolean isHopeful();
	void setHopeful(boolean hopeful);
	Integer[] getSolution() throws SudokuSolverFatalException;
	Cell getLastCellTested();
	void setLastCellTested(Cell lastCellTested);
	Integer getHighestMissingCellNumber();
	boolean isSolved();
	void setSolved(boolean solved);
	Integer[] getCellsAsIntegerArray();
}
