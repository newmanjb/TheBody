package com.noomtech.thebody.communication;

/**
 * Represents the API that the server-side body uses to communicate with any clients.
 * @author Nooom, Noomtech Ltd
 */
public interface Communicator 
{

	
	public void postEvent(Object notifier, String message, String category);
	
	public void postExceptionEvent(String message, Exception e);
}
