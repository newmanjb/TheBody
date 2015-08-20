package com.noomtech.thebody.circulatorysystem;


import java.util.Set;

import com.noomtech.thebody.buildingblocks.Particle;
import com.noomtech.thebody.buildingblocks.ShockWaveEvent;
import com.noomtech.thebody.buildingblocks.SingleConnectorPipe;
import com.noomtech.thebody.communication.Communicator;
import com.noomtech.thebody.communication.CommunicatorFactory;


public class Atrium extends SingleConnectorPipe
{

	
	private static final Communicator COMMUNICATOR = CommunicatorFactory.getCommunicator();
	private int beatForce;
	
	
	//For hibernate
	private Atrium()
	{
		super();
	}
	
	public Atrium(String name, Set<Particle> particles)
	{
		super(name, particles);		
	}
		
	
	void setBeatForce(int beatForce)
	{
		this.beatForce = beatForce;
	}
			
	
	void beat()
	{
		//Build a shockwave and send it.  It will first move particles out of the heart and then be
		//propogated down the pipes etc..
		ShockWaveEvent heartBeat = new ShockWaveEvent(this, beatForce);
		COMMUNICATOR.postEvent(this, "HeartBeat", "Heart.Heartbeat");
		shockWaveEvent(heartBeat);			
	}
}