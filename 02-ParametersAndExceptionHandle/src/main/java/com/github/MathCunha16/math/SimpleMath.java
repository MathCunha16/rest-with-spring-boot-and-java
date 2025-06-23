package com.github.MathCunha16.math;

import com.github.MathCunha16.exceptions.UnsupportedMathOperationException;

public class SimpleMath {

	public Double sum(Double num1, Double num2) {
		return num1 +  num2;
	}
	
	public Double sub(Double num1, Double num2) {
		return num1 -  num2;
	}
	
	public Double mult(Double num1, Double num2) {
		return num1 * num2;
	}
	
	public Double div(Double num1, Double num2) {
		if(num2 == 0) {
			throw new UnsupportedMathOperationException("Cannot divide by 0!");
		} 
		return num1 / num2;
	}

	public Double avg(Double num1, Double num2) {
		return (num1 +  num2) / 2;
	}
	
	public Double sqrt(Double num1) {
		return Math.sqrt(num1);
	}
	
}
