package hci.gnomex.utility.exceptions;

@SuppressWarnings("serial")
public class ProductException extends Exception {
	
	public ProductException() {
		super();
	}
	
	public ProductException(String message) {
		super(message);
	}
	
	public ProductException(Throwable cause) {
		super(cause);
	}
	
	public ProductException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ProductException(String message, Throwable cause, 
							boolean enableSuppression, boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
