package com.noomtech.thebody.communication;

/**
 * Simple {@link Communicator} implementation that simply writes to the console
 * @author Nooom
 */
public class ConsoleCommunicator extends AbstractCommunicator implements Communicator
{

	private static final String SERIOUS_ISSUE = "Serious Issue";

	
	public void postEvent(Object notifier, String message, String subject)
	{
		System.out.println("(" + notifier + ") - " + subject + " - " + message);
		super.fireEvent(subject, message);
	}

	public void postExceptionEvent(String message, Exception e)
	{
		System.out.println(SERIOUS_ISSUE);
		e.printStackTrace();
	}
}
