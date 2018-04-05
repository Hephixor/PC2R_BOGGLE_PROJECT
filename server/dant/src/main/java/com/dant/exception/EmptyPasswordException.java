package com.dant.exception;

public class EmptyPasswordException extends Exception{
	
	public EmptyPasswordException(){
		super("Veuillez renseigner un mot de passe.");
	}

}
