package hci.gnomex.utility.exceptions;

import hci.framework.control.RollBackCommandException;

@SuppressWarnings("serial")
public class GNomExRollbackException extends RollBackCommandException {
	
	private static final boolean 	DEFAULT_ROLLBACK_POLICY = true;
	
	private boolean 				requiresRollback;
	private String 					displayFriendlyMessage;
	
	public GNomExRollbackException(String message, boolean requiresRollback, 
			String displayFriendlyMessage) {
		
		super(message);
		
		this.requiresRollback = requiresRollback;
		this.displayFriendlyMessage = displayFriendlyMessage;
	}
	
	public GNomExRollbackException() {
		this(null, DEFAULT_ROLLBACK_POLICY, null);
	}
	
	public boolean getRequiresRollback() {
		return requiresRollback;
	}
	
	public String getDisplayFriendlyMessage() {
		return displayFriendlyMessage;
	}

}
