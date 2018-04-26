package it.islandofcode.jvtllib.model.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import it.islandofcode.jvtllib.model.Scalar;

/**
 * @author Pier Riccardo Monzo
 */
public class Number {
	
	/* SOLO SCALARI */
	
	public static Scalar add(Scalar a, Scalar b) {
		Scalar ret = null;
		if(a.isNumber() && b.isNumber()) {
			if(a.getScalarType().equals(Scalar.SCALARTYPE.Float) || b.getScalarType().equals(Scalar.SCALARTYPE.Float)) {
				return new Scalar(""+(a.asDouble()+b.asDouble()), Scalar.SCALARTYPE.Float);
			}
			return new Scalar(""+(a.asDouble()+b.asDouble()), Scalar.SCALARTYPE.Integer);
		} else 
			throw new IllegalArgumentException("SUM of non numerical value.");
	}
	
	public static Scalar mul(Scalar a, Scalar b) {
		Scalar ret = null;
		if(a.isNumber() && b.isNumber()) {
			if(a.getScalarType().equals(Scalar.SCALARTYPE.Float) || b.getScalarType().equals(Scalar.SCALARTYPE.Float)) {
				return new Scalar(""+(a.asDouble()*b.asDouble()), Scalar.SCALARTYPE.Float);
			}
			return new Scalar(""+(a.asDouble()*b.asDouble()), Scalar.SCALARTYPE.Integer);
		} else 
			throw new IllegalArgumentException("MUL of non numerical value.");
	}
	
	public static Scalar sub(Scalar a, Scalar b) {
		Scalar ret = null;
		if(a.isNumber() && b.isNumber()) {
			if(a.getScalarType().equals(Scalar.SCALARTYPE.Float) || b.getScalarType().equals(Scalar.SCALARTYPE.Float)) {
				return new Scalar(""+(a.asDouble()-b.asDouble()), Scalar.SCALARTYPE.Float);
			}
			return new Scalar(""+(a.asDouble()-b.asDouble()), Scalar.SCALARTYPE.Integer);
		} else if(a.getScalarType()==Scalar.SCALARTYPE.Date && b.getScalarType()==Scalar.SCALARTYPE.Date) {
			return new Scalar(""+Duration.between(a.asDate().getDate(),  b.asDate().getDate()).toDays(), Scalar.SCALARTYPE.Integer);
		} else 
			throw new IllegalArgumentException("SUB of non numerical or non date value.");
	}
	
	public static Scalar div(Scalar a, Scalar b) {
		Scalar ret = null;
		if(a.isNumber() && b.isNumber()) {
			if(b.asDouble()==0) {
				throw new RuntimeException("Division by zero!");
			}
			if(a.getScalarType().equals(Scalar.SCALARTYPE.Float) || b.getScalarType().equals(Scalar.SCALARTYPE.Float)) {
				return new Scalar(""+(a.asDouble()*b.asDouble()), Scalar.SCALARTYPE.Float);
			}
			return new Scalar(""+(a.asDouble()*b.asDouble()), Scalar.SCALARTYPE.Integer);
		} else 
			throw new IllegalArgumentException("Div of non numerical value.");
	}
	
	/* COLONNA SCALARI */
	
	
	/* DATASET DATASET */
	
	

}