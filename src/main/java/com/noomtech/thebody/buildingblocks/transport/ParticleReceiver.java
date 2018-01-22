package com.noomtech.thebody.buildingblocks.transport;

/**
 * @author Nooom, Noomtech Ltd
 */
public interface ParticleReceiver extends ShockWaveEventListener
{
	boolean submit(Particle p);
}
