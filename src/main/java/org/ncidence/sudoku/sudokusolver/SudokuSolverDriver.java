package org.ncidence.sudoku.sudokusolver;

import java.util.Date;

import org.ncidence.sudoku.game.Grid;
import org.ncidence.sudoku.game.IGrid;

import com.google.gson.Gson;

public class SudokuSolverDriver {
	
	
	private Date startTime;
	private Date endTime;
	
	
	
	public static void main(String[] args){
		if(args != null && args.length > 0){
			SudokuSolverDriver sudokuSolver = new SudokuSolverDriver();
			sudokuSolver.solveCsv(args[0]);
		}
	}
	
	public String solveCsv(String csv){
		System.out.println("SudokuSolver start");
		startTime = new Date();
		if(csv != null)
			return solveArray(csv.split(","));
		
		System.out.println("csv was null");
		return getSolutionJson(null);
	}
	
	public String solveArray(String[] inputArray){
		Integer[] solution = null;
		if(inputArray != null){
			IGrid grid = new Grid();
			try {
				grid.init(inputArray);
				solution = grid.getSolution();
			} catch (SudokuSolverFatalException e) {
				System.out.println(e.getMessage());
			}
		}else{
			System.out.println("inputArray was null");
		}
		
		return getSolutionJson(solution);
	}

	
	
	//region PRIVATE HELPERS
	private String getSolutionJson(Integer[] solution){
		endTime = new Date();
		double seconds = ((endTime.getTime() - startTime.getTime())/1000.0);
		if(solution != null){
			System.out.println("Solved in: " + seconds + " sec");
			String csv = null;
			for(Integer val : solution){
				if(csv == null){
					csv = val.toString();
				}else{
					csv += "," + val.toString();
				}
			}
			String returnValue = SERIALIZER.toJson(new SolutionResponse(csv));
			System.out.println("Solution: " + returnValue);
			return returnValue;
		}else{
			System.out.println("Failed to solve in: " + seconds + " sec");
			return SERIALIZER.toJson(ERROR_RESPONSE);
		}
	}
	//endregion PRIVATE HELPERS
	
	
	//region STATIC HELPERS
	public static String ERROR_MESSAGE = "cannot be completed";
	public static Gson SERIALIZER = new Gson();
	public static ErrorResponse ERROR_RESPONSE = new ErrorResponse(ERROR_MESSAGE);
	//endregion STATIC HELPERS
	
}