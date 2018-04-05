package com.dant.exception;

public class UserNotFoundException extends Exception{
	
	public UserNotFoundException(){
		super("Aucun utilisateur trouvé.");
	}

}
