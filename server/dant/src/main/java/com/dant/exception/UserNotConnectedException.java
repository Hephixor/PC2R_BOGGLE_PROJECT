package com.dant.exception;

public class UserNotConnectedException extends Exception{
	
	public UserNotConnectedException(){
		super("Vous n'êtes pas connecté.");
	}

}
