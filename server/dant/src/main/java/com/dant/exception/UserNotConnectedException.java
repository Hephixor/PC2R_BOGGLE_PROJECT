package com.dant.exception;

public class UserNotConnectedException extends Exception{
	
	public UserNotConnectedException(){
		super("Vous n'�tes pas connect�.");
	}

}
