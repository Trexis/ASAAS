package net.trexis.asaas.web.commons;

public class ItemNotFoundException extends Exception {

	public ItemNotFoundException(String itemName){
		super(itemName + " not found.");
	}

	public ItemNotFoundException(Throwable cause){
		super("Item not found.", cause);
	}
	
	public ItemNotFoundException(String itemName, Throwable cause){
		super(itemName + " not found.", cause);
	}
}
