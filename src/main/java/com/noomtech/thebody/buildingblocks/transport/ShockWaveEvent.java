package com.noomtech.thebody.buildingblocks.transport;

/**
 * Represents a shockwave e.g. a heartbeat, that starts in one location (the source) e.g. the heart, and which can be passed on 
 * to other locations or components depending on their connections, their shockwave absorbance, the strength of the shockwave etc.. 
 * @author Nooom, Noomtech Ltd
 */
public final class ShockWaveEvent 
{

	private final int force;
	private Object source;
	
	public ShockWaveEvent(Object source, int force)
	{
		this.force = force;
		this.source = source;
	}
	
	
	public int getForce()
	{
		return force;
	}
	
	public Object getSource()
	{
		return source;
	}	
}
