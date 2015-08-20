package com.noomtech.thebody.communication;

/**
 * Simple Communicator implementation that simply writes to the console
 * @author Nooom
 */
public class ConsoleCommunicator implements Communicator 
{

	private static final String SERIOUS_ISSUE = "Serious Issue";
	
	
	public void postEvent(Object notifier, String message, String category)
	{
		System.out.println("(" + notifier + ") - " + category + " - " + message);
	}
	
	public void postExceptionEvent(String message, Exception e)
	{
		System.out.println(SERIOUS_ISSUE);
		e.printStackTrace();
	}
}
