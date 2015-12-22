package com.noomtech.thebody.communication;

/**
 * Represents the API that the server-side body uses to communicate with any clients.
 * @author Nooom, Noomtech Ltd
 */
public interface Communicator 
{

	
	void postEvent(Object notifier, String message, String category);
	
	void postExceptionEvent(String message, Exception e);

	void addEventListener(CommunicatorEventListener listener, String subject);
}
