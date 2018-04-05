package com.dant.exception;

public class InvalidEmailException extends Exception{
	
	public InvalidEmailException(){
		super("Veuillez renseigner un email valide.");
	}

}
