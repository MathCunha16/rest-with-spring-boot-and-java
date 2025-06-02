package com.github.MathCunha16.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.MathCunha16.converters.NumberConverter;
import com.github.MathCunha16.exceptions.UnsupportedMathOperationException;
import com.github.MathCunha16.math.SimpleMath;

@RestController
@RequestMapping("/math")
public class MathController {
	
	private SimpleMath math = new SimpleMath();
	
	// localhost:8080/math/sum/3/5
	@RequestMapping("/sum/{num1}/{num2}") // Soma
	public Double sum(
			@PathVariable(value = "num1") String num1, // se o nome da variavel for o mesmo nem precisa settar o valor
			@PathVariable(value = "num2") String num2 // mas vou deixar esse só de exemplo pra estudos futuros
		) throws Exception{
		
		if(!NumberConverter.isNumeric(num1) || !NumberConverter.isNumeric(num2)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		return math.sum(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
	}
	
	// localhost:8080/math/sub/3/5
	@RequestMapping("/sub/{num1}/{num2}") // subtração
	public Double subtraction(
			@PathVariable String num1,
			@PathVariable String num2
			) throws Exception{
		
		if(!NumberConverter.isNumeric(num1) || !NumberConverter.isNumeric(num2)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		return math.sub(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
	}
	
	// localhost:8080/math/mult/3/5
	@RequestMapping("/mult/{num1}/{num2}") // multiplicação
	public Double multiplication(
			@PathVariable String num1,
			@PathVariable String num2
			) throws Exception{
		
		if(!NumberConverter.isNumeric(num1) || !NumberConverter.isNumeric(num2)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		return math.mult(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
	}
	
	// localhost:8080/math/div/3/5
	@RequestMapping("/div/{num1}/{num2}") // divisão
	public Double division(
			@PathVariable String num1,
			@PathVariable String num2
			) throws Exception{
		
		if(!NumberConverter.isNumeric(num1) || !NumberConverter.isNumeric(num2)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		return math.div(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
	}

	// localhost:8080/math/avg/3/5
	@RequestMapping("/avg/{num1}/{num2}") // média
	public Double mean(
			@PathVariable String num1,
			@PathVariable String num2
		) throws Exception{
		
		if(!NumberConverter.isNumeric(num1) || !NumberConverter.isNumeric(num2)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		return math.avg(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
	}
	
	// localhost:8080/math/sqrt/3/5
	@RequestMapping("/sqrt/{num1}") // raiz quadrada
	public Double squareRoot(
			@PathVariable String num1
			) throws Exception{
		
		if(!NumberConverter.isNumeric(num1)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		return math.sqrt(NumberConverter.convertToDouble(num1));
	}
}
