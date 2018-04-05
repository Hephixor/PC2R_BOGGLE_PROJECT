package com.dant.exception;

public class EmptyEmailException extends Exception{

	public EmptyEmailException(){
		super("Veuillez renseigner un email");
	}
}
