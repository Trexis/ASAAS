package net.trexis.asaas.web.commons;

public class ItemNotFoundException extends Exception {

	public ItemNotFoundException(String message){
		super(message);
	}

	public ItemNotFoundException(Throwable cause){
		super("Item not found.", cause);
	}
	
	public ItemNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
