package com.dant.exception;

public class InvalidUserKeyException extends Exception{
	
  public InvalidUserKeyException(){
	  super("Format de cl� utilisateur invalide.");
  }

}
