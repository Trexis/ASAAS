package net.trexis.asaas.web.commons;

public class NotOwnerException extends Exception {

	public NotOwnerException(String message){
		super(message);
	}

	public NotOwnerException(Throwable cause){
		super("You are not the owner.", cause);
	}
	
	public NotOwnerException(String message, Throwable cause){
		super(message, cause);
	}
}
