package com.noomtech.thebody.circulatorysystem;


import java.util.Set;

import com.noomtech.thebody.buildingblocks.transport.Particle;
import com.noomtech.thebody.buildingblocks.transport.ShockWaveEvent;
import com.noomtech.thebody.buildingblocks.transport.SingleConnectorPipe;
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
		if(beatForce > 0) {
			//Build a shockwave and send it.  It will first move particles out of the heart and then be
			//propogated down the pipes etc..
			ShockWaveEvent heartBeat = new ShockWaveEvent(this, beatForce);
			COMMUNICATOR.postEvent(this, "HeartBeat", Heart.HEART_BEAT_EVENT);
			shockWaveEvent(heartBeat);
		}
	}
}