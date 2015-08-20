package com.noomtech.thebody.buildingblocks;

import java.util.Set;


/**
 * Represents a pipe that has two outbound connections i.e. a V or T at its end
 * @author Nooom, Noomtech Ltd
 */
public class DoubleConnectorPipe extends Pipe implements DoubleOutboundConnector
{

	private Pipe leftConnection;
	private Pipe rightConnection;
	private boolean useLeftPipe = false;
	private final Object useLeftPipeMutex = new Object();
	
	
	//For hibernate
	private DoubleConnectorPipe()
	{
		super();
	}
	
	public DoubleConnectorPipe(String name, Set<Particle> particles)
	{
		super(name, particles);
	}
	
	boolean passToForwardConnection(Particle p)
	{				
		ParticleReceiver pipeToUse = null;
		ParticleReceiver otherPipeToUse = null;
		//Switch between which pipe to pass the particle to on a 50/50 basis
		synchronized(useLeftPipeMutex)
		{							
			pipeToUse = (useLeftPipe ? leftConnection : rightConnection);
			otherPipeToUse = (useLeftPipe ? rightConnection : leftConnection);
			useLeftPipe = !useLeftPipe;
		}
				
		if(!pipeToUse.submit(p) && !otherPipeToUse.submit(p))
		{
			return false;
		}
		
		return true;
	}
	
	void passOnShockWaveToConnections(int forceForNextPipe, Object source)
	{
		//Prevent infinite loops
		if(leftConnection != source)
		{
			leftConnection.shockWaveEvent(new ShockWaveEvent(source, forceForNextPipe));
		}
		//Prevent infinite loops
		if(rightConnection != source)
		{
			rightConnection.shockWaveEvent(new ShockWaveEvent(source, forceForNextPipe));
		}
	}	
	
	public void setForwardConnection1(Pipe leftConnection)
	{
		this.leftConnection = leftConnection;
	}
	
	public void setForwardConnection2(Pipe rightConnection)
	{
		this.rightConnection = rightConnection;
	}	
	
	public Pipe getForwardConnection1()
	{
		return leftConnection;
	}
	
	private Pipe getForwardConnection2()
	{
		return rightConnection;
	}	
}