package com.noomtech.thebody.buildingblocks;

import java.util.Set;


/**
 * Represents a pipe that only has one outbound connection
 * @author Nooom, Noomtech Ltd
 */
public class SingleConnectorPipe extends Pipe implements SingleOutboundConnector
{
    private Pipe forwardConnection;
	
	//For hibernate
	protected SingleConnectorPipe()
	{
		super();
	}
	
	public SingleConnectorPipe(String name, Set<Particle> particles)
	{
		super(name, particles);
	}	
	
	boolean passToForwardConnection(Particle p)
	{		 
		if(!forwardConnection.submit(p))
		{
			return false;
		}
		
		return true;
	}
	
	void passOnShockWaveToConnections(int forceForNextPipe, Object source)
	{
		//Prevent infinite loops
		if(forwardConnection != source)
		{
			forwardConnection.shockWaveEvent(new ShockWaveEvent(source, forceForNextPipe));
		}
	}
	
	public void setForwardConnection1(Pipe connection)
	{
		this.forwardConnection = connection;
	}
	
	public Pipe getForwardConnection1()
	{
		return forwardConnection;
	}
}