package com.dant.exception;

public class EmptyNameException extends Exception{

	public EmptyNameException(){
		super("Veuillez renseigner votre nom et prénom.");
	}
}
