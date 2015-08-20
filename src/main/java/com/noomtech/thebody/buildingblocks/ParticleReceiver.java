package com.noomtech.thebody.buildingblocks;

/**
 * @author Nooom, Noomtech Ltd
 */
public interface ParticleReceiver extends ShockWaveEventListener
{
	public boolean submit(Particle p);
}
