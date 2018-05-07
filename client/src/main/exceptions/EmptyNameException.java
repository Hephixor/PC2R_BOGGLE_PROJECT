package main.exceptions;

public class EmptyNameException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmptyNameException(String err) {
		super(err);
	}
}
