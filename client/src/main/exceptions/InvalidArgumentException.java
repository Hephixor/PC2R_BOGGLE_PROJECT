package main.exceptions;

public class InvalidArgumentException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public InvalidArgumentException(String err){
		super(err);
	}

}
