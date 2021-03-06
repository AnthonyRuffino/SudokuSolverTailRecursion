package org.ncidence.sudoku.game;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.ncidence.sudoku.sudokusolver.SudokuSolverFatalException;

public class Grid implements IGrid {

	//x,x,x,2,6,x,7,x,1,6,8,x,x,7,x,x,9,x,1,9,x,x,x,4,5,x,x,8,2,x,1,x,x,x,4,x,x,x,4,6,x,2,9,x,x,x,5,x,x,x,3,x,2,8,x,x,9,3,x,x,x,7,4,x,4,8,9,5,x,x,3,6,7,x,3,x,1,8,x,x,x
	//8,x,x,x,x,x,x,x,x,x,x,3,6,x,x,x,x,x,x,7,x,x,9,x,2,x,x,x,5,x,x,x,7,x,x,x,x,x,x,x,4,5,6,x,x,x,x,x,1,x,x,x,3,x,x,x,1,x,x,x,x,6,8,x,x,8,5,x,x,x,1,x,x,9,x,x,x,x,4,x,x
	private Cell[] cells;
	private IGrid lastHopefulPredecessor = null;
	private boolean hopeful = true;
	private Cell lastCellTested = null;
	private Integer highestMissingCellNumber = null;
	private boolean isSolved = false;

	public void init(String[] cellsAsStrings) throws SudokuSolverFatalException {
		if (cellsAsStrings == null)
			throw new SudokuSolverFatalException("Grid is null (cellsAsStrings).");

		if (cellsAsStrings.length != 81)
			throw new SudokuSolverFatalException("Grid has " + (cellsAsStrings.length < 81 ? "less" : "more")
					+ " than 81 cells: " + cellsAsStrings.length);

		this.cells = new Cell[cellsAsStrings.length];

		Map<Integer, Box> boxMap = new HashMap<Integer, Box>();
		Map<Integer, HorizontalChute> horizontalChuteMap = new HashMap<Integer, HorizontalChute>();
		Map<Integer, VerticalChute> verticalChuteMap = new HashMap<Integer, VerticalChute>();

		int currentRowNumber = 1;
		int currentColumnNumber = 0;

		for (Integer cellNumber = 0; cellNumber < cellsAsStrings.length; cellNumber++) {

			String cellAsString = cellsAsStrings[cellNumber];

			currentColumnNumber++;

			if (currentColumnNumber > 9) {
				currentRowNumber++;
				currentColumnNumber = 1;
			}

			if (currentRowNumber > 9)
				throw new SudokuSolverFatalException("More than 9 rows detected");

			Box box = null;
			HorizontalChute horizontalChute = null;
			VerticalChute verticalChute = null;

			// BOX
			Integer boxNumber = getBoxNumber(currentColumnNumber, currentRowNumber);

			if (boxNumber == null) {
				throw new SudokuSolverFatalException(
						new MessageFormat("Unable to determine box number for row:{0}, column:{1}")
								.format(new Object[] { currentRowNumber, currentColumnNumber }));
			}

			if (boxMap.containsKey(boxNumber)) {
				box = boxMap.get(boxNumber);
			} else {
				box = new Box();
				boxMap.put(boxNumber, box);
			}

			// HORIZONTAL CHUTE
			if (horizontalChuteMap.containsKey(currentRowNumber)) {
				horizontalChute = horizontalChuteMap.get(currentRowNumber);
			} else {
				horizontalChute = new HorizontalChute();
				horizontalChuteMap.put(currentRowNumber, horizontalChute);
			}

			// VERTICAL CHUTE CHUTE
			if (verticalChuteMap.containsKey(currentColumnNumber)) {
				verticalChute = verticalChuteMap.get(currentColumnNumber);
			} else {
				verticalChute = new VerticalChute();
				verticalChuteMap.put(currentColumnNumber, verticalChute);
			}

			Cell cell = new Cell(cellNumber + 1, cellAsString, box, horizontalChute, verticalChute);
			cells[cellNumber] = cell;

			if (cell.getIntValue() == null
					&& (highestMissingCellNumber == null || highestMissingCellNumber < cell.getCellNumber())) {
				highestMissingCellNumber = cell.getCellNumber();
			}
		}
	}

	public void init(IGrid predecessor) throws SudokuSolverFatalException {
		this.lastHopefulPredecessor = predecessor.isHopeful() ? predecessor : predecessor.getLastHopefulPredecessor();

		String[] cellsAsString = null;

		Cell[] predecessorsCells = predecessor.getCells();
		if (predecessorsCells != null) {
			cellsAsString = new String[predecessorsCells.length];

			for (Integer cellNumber = 0; cellNumber < predecessorsCells.length; cellNumber++) {
				Cell cell = predecessorsCells[cellNumber];
				cellsAsString[cellNumber] = cell != null ? cell.getStringValue() : null;
			}
		}

		init(cellsAsString);
	}

	public Cell[] getCells() {
		return cells;
	}

	public IGrid getLastHopefulPredecessor() {
		return lastHopefulPredecessor;
	}

	public boolean isHopeful() {
		return hopeful;
	}

	public void setHopeful(boolean hopeful) {
		this.hopeful = hopeful;
	}

	public Cell getLastCellTested() {
		return lastCellTested;
	}

	public void setLastCellTested(Cell lastCellTested) {
		this.lastCellTested = lastCellTested;
	}

	public Integer getHighestMissingCellNumber() {
		return highestMissingCellNumber;
	}

	public Integer[] getCellsAsIntegerArray() {
		Integer[] intArray = new Integer[81];
		for (Integer cellNumber = 0; cellNumber < cells.length; cellNumber++) {
			Cell cell = cells[cellNumber];
			intArray[cellNumber] = cell.getIntValue();
		}
		return intArray;
	}

	public boolean isSolved() {
		return isSolved;
	}

	public void setSolved(boolean solved) {
		isSolved = solved;
	}

	public Integer[] getSolution() throws SudokuSolverFatalException {
		IGrid currentGrid = this;

		do {
			currentGrid = next(currentGrid);

			if (currentGrid == null) {
				return null;
			} else if (currentGrid.isSolved()) {
				return currentGrid.getCellsAsIntegerArray();
			}
		} while (true);
	}

	public static IGrid next(IGrid last) throws SudokuSolverFatalException {

		if (last.getCells() == null)
			throw new SudokuSolverFatalException("Grid is null (cells).");

		if (last.getCells().length != 81)
			throw new SudokuSolverFatalException("Grid has less than 81 cells.");

		Cell cellToTest = null;

		if (last.getLastCellTested() == null) {
			for (Integer cellNumber = 0; cellNumber < last.getCells().length; cellNumber++) {
				Cell cell = last.getCells()[cellNumber];
				if (cell == null)
					throw new SudokuSolverFatalException("Null cell detected.");
				if (cell.getIntValue() == null) {
					cellToTest = cell;
					cellToTest.setValue(1);
					last.setLastCellTested(cellToTest);
					break;
				}
			}
		} else {
			last.getLastCellTested().setValue(last.getLastCellTested().getIntValue() + 1);
			cellToTest = last.getLastCellTested();
		}

		if (cellToTest == null)
			throw new SudokuSolverFatalException("Cell to solve was null");

		boolean isLastCell = last.getHighestMissingCellNumber() != null
				&& last.getHighestMissingCellNumber().equals(cellToTest.getCellNumber());

		if (cellToTest.getIntValue() > 9) {
			System.out.println(">9");
		}

		for (Integer i = cellToTest.getIntValue(); i <= 9; i++) {
			cellToTest.setValue(i);
			if (cellToTest.isValid()) {
				if (isLastCell) {
					System.out.println("Solution found.");
					last.setSolved(true);
					return last;
				} else {
					last.setHopeful(i < 9);
					IGrid grid = new Grid();
					grid.init(last);
					return grid;
				}
			} else if (i.equals(9)) {
				last.setHopeful(false);
				if (isLastCell) {
					if (last.getLastHopefulPredecessor() != null) {
						System.out.println("On last cell and going back to a hopeful predessor.");
						return last.getLastHopefulPredecessor();
					}
					System.out.println("No more hopeful predecessors [last cell].");
					return null;
				} else {
					if (last.getLastHopefulPredecessor() != null) {
						System.out.println("Calling hopeful predecessor.");
						return last.getLastHopefulPredecessor();
					}
					System.out.println("No more hopeful predecessors.");
					return null;
				}
			}
		}

		throw new SudokuSolverFatalException("End reached with no solution");
	}

	// region STATIC HELPERS
	public static Integer getBoxNumber(int column, int row) {
		Integer boxNumber = null;

		if (column < 4) {
			if (row < 4) {
				boxNumber = 1;
			} else if (row < 7) {
				boxNumber = 4;
			} else {
				boxNumber = 7;
			}
		} else if (column < 7) {
			if (row < 4) {
				boxNumber = 2;
			} else if (row < 7) {
				boxNumber = 5;
			} else {
				boxNumber = 8;
			}
		} else {
			if (row < 4) {
				boxNumber = 3;
			} else if (row < 7) {
				boxNumber = 6;
			} else {
				boxNumber = 9;
			}
		}

		return boxNumber;
	}
	// endregion PRIVATE HELPERS

}