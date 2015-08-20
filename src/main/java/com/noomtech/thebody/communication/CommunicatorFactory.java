package com.noomtech.thebody.communication;


/**
 * @todo config (Spring?) to get right communicator factory
 */
public class CommunicatorFactory 
{
	private static final class InstanceHolder
	{
		private static final Communicator instance = new ConsoleCommunicator();
	}
	
	
	public static Communicator getCommunicator()
	{
		return InstanceHolder.instance;
	}
}
