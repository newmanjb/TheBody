package com.noomtech.thebody.buildingblocks;

/**
 * @author Nooom, Noomtech Ltd
 */
public interface ParticleProcessor 
{
	/**
	 * Careful with the threading on this.  Check the thread that is calling this to make sure that, if 
	 * delays are likely while processing the particle, that it's OK to hold up the calling thread e.g. with Particle instances 
	 * moving though pipes the thread that
	 * calls this is the particle's own movement thread, so it's OK to hold it up if necessary, as this would represent the
	 * particle itself being held up and not anything else.
	 * @return The particle, modified or not, or null if this processor needs to take the particle 
	 */
	public Particle processParticle(Particle p);
}
