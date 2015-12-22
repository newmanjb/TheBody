package com.noomtech.thebody.buildingblocks.transport;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.noomtech.thebody.buildingblocks.Named;
import com.noomtech.thebody.communication.Communicator;
import com.noomtech.thebody.communication.CommunicatorFactory;
import com.noomtech.thebody.utils.ConcurrentHashSet;


/**
 * Superclass that represents a container that receives and holds particles.  A pipe also has a link to a forward connection, which is another
 * Pipe 
 * @author Nooom, Noomtech Ltd
 */
public abstract class Pipe implements ParticleReceiver, Named
{

	private ConcurrentHashSet<Particle> particles = new ConcurrentHashSet<Particle>();
	//Added in the same way as listeners.  They examine, mutate or pick up particles e.g. receptors looking for
	//red blood cells with oxygen
	private final List<ParticleProcessor> particleProcessors = new CopyOnWriteArrayList<ParticleProcessor>();
	private static final Communicator COMMUNICATOR = CommunicatorFactory.getCommunicator();
	private String name;
	private int id;
	private final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	protected float viscosity = 0.1f;

	
	//For hibernate
	protected Pipe()
	{				
	}
	
	
	public Pipe(String name, Set<Particle> particles)
	{
		setName(name);
		if(particles != null)
		{
			setParticles(particles);
		}
	}
		
	public final String getName()
	{
		return name;
	}
	
	private final void setName(String name)
	{
		this.name = name;
	}
	
	private final int getId()
	{
		return id;
	}
	
	private final void setId(int id)
	{
		this.id = id;
	}
	
	private final Set<Particle> getParticles()
	{
		return particles;
	}
	
	private final void setParticles(Set<Particle> newParticles)
	{
		Iterator<Particle> iter = newParticles.iterator();		
		while(iter.hasNext())
		{
			addToParticleSet(iter.next());
		}
	}
	
	private final void setViscosity(float viscosity)
	{
		this.viscosity = viscosity;
	}

	final float getViscosity()
	{
		return viscosity;
	}

	
	public final void shockWaveEvent(final ShockWaveEvent s)
	{		
		//Each shockwave is handled by its own thread
		singleThreadExecutor.execute(new Runnable()
		{
			public void run()
			{		
				int forceFromShockwaveAffectingThisPipe = (s.getForce());
				if(forceFromShockwaveAffectingThisPipe > 0)
				{
					//Propagate the shockwave
					passOnShockWaveToConnections(forceFromShockwaveAffectingThisPipe, s.getSource());
					
					//Pass the shockwave on the any particles in the pipe
					Iterator<Particle> iter = particles.iterator();
					while(iter.hasNext())
					{
						iter.next().setMomentum(forceFromShockwaveAffectingThisPipe);
					}
				}
			}
			});
	}	
		
	/**
	 * Called when the particle enters the pipe.
	 * @return true if it is successfully added to the pipe, false otherwise e.g. if the pipe is blocked.
	 */
	public boolean submit(final Particle particle)
	{
		//�Reject here if pipe is blocked?

        singleThreadExecutor.execute( new Runnable() {

            public void run() {
                COMMUNICATOR.postEvent(getName(), "Received a particle (" + particle + ")", "Particle.Received");

                addToParticleSet(particle);
                //This is where the particle is passed through the processors who may or may not be interested in it
                Particle processedParticle = processParticle(particle);
                if(processedParticle != null)
                {
                    //If none of the particle processors have taken the particle then the particle can move on  or stay
                    //depending on its momentum
                    processedParticle.sayItCanMove();
                }
                else {
                    removeFromParticleSet(particle);
                }
            }

        });

		return true;
	}
	
	public void addParticleProcessor(ParticleProcessor processor)
	{
		particleProcessors.add(processor);
	}
	
	@Override
	public String toString()
	{
		return name;
	}

	private Particle processParticle(Particle particle)
	{
		//Use the command chain pattern.  Note that input of one processor should NEVER
		//be in any way dependent on the output of another, as the processors are not called 
		//in any particular order.  A processor that takes the particle returns null
		int size = particleProcessors.size();
		int ctr = 0;
		while(particle != null && ctr < size)
		{
			particle = particleProcessors.get(ctr).processParticle(particle);
			ctr++;
		}

        return particle;
	}
	
	private final void addToParticleSet(Particle p)
	{
		if(!particles.add(p))
		{
			//throw new IllegalStateException("Particle " + p + " is already in particle set for " + this);
		}
		p.setContainer(this);
	}
	
	private final void removeFromParticleSet(Particle p)
	{
		if(!particles.remove(p))
		{
			throw new IllegalStateException("Particle " + p + " was never in particle set for " + this);
		}
		p.setContainer(null);
	}

	//Called by the particles when they wish to be passed to whatever this pipe is connected to e.g. when they
	//have been processed by this pipe and still have momentum.
	//Returns false if the particle could not be passed to the next connection
	final boolean passToNextConnection(Particle p)
	{
		removeFromParticleSet(p);
		boolean passedToNextConnection = passToForwardConnection(p);
		if(!passedToNextConnection)
		{
			addToParticleSet(p);
		}

        return passedToNextConnection;
	}
	
	//Moves the particle to the next connection.  If this isn't possible then false should be returned
	abstract boolean passToForwardConnection(Particle p);
	
	//Propagates the shockwave to the pipe connected to this one
	abstract void passOnShockWaveToConnections(int force, Object source);

    public abstract Pipe getForwardConnection1();
}