package org.ncidence.sudoku.game;

import java.util.HashSet;
import java.util.Set;

public class SetOfNineCells {
	protected Set<Integer> values;
	
	protected SetOfNineCells(){
		values = new HashSet<Integer>();
	}
	
	public boolean valueExists(Integer valueToTest){
		return values.contains(valueToTest);
	}
	
	public void addValue(Integer valueToAdd){
		values.add(valueToAdd);
	}
}
