package org.ncidence.sudoku.game;

import org.ncidence.sudoku.sudokusolver.SudokuSolverFatalException;

public class Cell {
	private int cellNumber;
	private String stringValue;
	private Integer intValue = null;
	
	private Box box;
	private HorizontalChute horizontalChute;
	private VerticalChute verticalChute;
	
	public Cell(int cellNumber, String stringValue, Box box, HorizontalChute horizontalChute, VerticalChute verticalChute) throws SudokuSolverFatalException{
		this.cellNumber = cellNumber;
		if(stringValue == null || stringValue.trim().isEmpty())
			throw new SudokuSolverFatalException("null or empty cell value detected");
		
		this.stringValue = stringValue.trim().toLowerCase();
		
		this.box = box;
		this.horizontalChute = horizontalChute;
		this.verticalChute = verticalChute;
		
		if(!stringValue.equals("x")){
			try {
				Integer tempInt = Integer.parseInt(stringValue);
				
				if(tempInt < 1 || tempInt > 9)
					throw new SudokuSolverFatalException("Invalid cell value detected: " + stringValue);
				
				this.intValue = tempInt;
				
				if(this.box.valueExists(intValue)){
					throw new SudokuSolverFatalException("Cell contains value already present in box: " + stringValue);
				}
				this.box.addValue(intValue);
				
				if(this.horizontalChute.valueExists(intValue)){
					throw new SudokuSolverFatalException("Cell contains value already present in horizontal chute: " + stringValue);
				}
				this.horizontalChute.addValue(intValue);
				
				if(this.verticalChute.valueExists(intValue)){
					throw new SudokuSolverFatalException("Cell contains value already present in vertical chute: " + stringValue);
				}
				this.verticalChute.addValue(intValue);
				
			} catch (NumberFormatException e) {
				throw new SudokuSolverFatalException("non numerical, non 'x' cell value detected: " + stringValue);
			}
		}
	}
	
	public Integer getIntValue(){
		return intValue;
	}
	
	public String getStringValue(){
		return stringValue;
	}
	
	public void setValue(Integer value){
		intValue = value;
		stringValue = intValue.toString();
	}
	
	public boolean isValid(){
		return !box.valueExists(intValue) && !horizontalChute.valueExists(intValue) && !verticalChute.valueExists(intValue);
	}

	public Box getBox() {
		return box;
	}

	public HorizontalChute getHorizontalChute() {
		return horizontalChute;
	}

	public VerticalChute getVerticalChute() {
		return verticalChute;
	}

	public int getCellNumber() {
		return cellNumber;
	}
	
	
	
	
	
	
}
