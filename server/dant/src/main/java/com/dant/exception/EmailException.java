package com.dant.exception;

public class EmailException extends Exception{
	
	public EmailException(){
		super("Cet email est déjà utilisé.");
	}

}
