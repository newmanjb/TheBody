package com.noomtech.thebody.buildingblocks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.noomtech.thebody.communication.Communicator;
import com.noomtech.thebody.communication.CommunicatorFactory;

/**
 * Superclass for a particle.
 * @author Nooom, Noomtech Ltd
 */
public class Particle implements Named 
{

	//The container that this particle is currently in
	private Pipe currentContainer;
	private String name = null;
	//Handles moving the particle.
	private final Thread momentumThread = new Thread(new MovementThread());
	//The movement that is currently being processed
	private AtomicInteger currentMomentum = new AtomicInteger(0);
	private int id;
	private static final Communicator COMMUNICATOR = CommunicatorFactory.getCommunicator();
	//Used to stop and start the movement
    private Object allowedToMoveMutex = new Object();
    private Object momentumMutex = new Object();
    private AtomicBoolean move = new AtomicBoolean(false);
		
	//For hibernate
	protected Particle()
	{	
		momentumThread.start();
	}
	
	public Particle(String name)
	{
		this();
		setName(name);
	}

	private void setId(int id)
	{
		this.id = id;
	}
	
	private int getId()
	{
		return id;
	}
	
	public final String getName()
	{
		return name;
	}

	private final void setName(String name)
	{
		this.name = name;
		momentumThread.setName("Movement - " + name);
	}
	
	@Override
	public String toString()
	{
		if(name != null)
		{
			return name;
		}
		else
		{
			return super.toString();
		}
	}
	
	final void setContainer(Pipe container)
	{
		this.currentContainer = container;
	}

	private final Pipe getContainer()
	{
		return currentContainer;
	}
	
	//Will move this particle to the next container at a speed dictated by the speed and
    //viscosity etc..  Called by the movement thread in this class.  The movement will only happen if there is
    //momentum.
	final void processMomentum()
	{
		if(currentMomentum.get() > 0)
		{
			//The particle loses momentum as it moves
			currentMomentum.decrementAndGet();

            //Stop moving and wait for the next contained (the one it's just been passed to) to start it moving
            //again after it processes it or whatever it does
            move.set(false);
			boolean passedToNextConnection = currentContainer.passToNextConnection(this);
            if(!passedToNextConnection) {
                //It could not be passed to the next connection for some reason, so keep moving until it is
                //either successfully passed on or we run out of momentum
                move.set(true);
            }
		}
	}
	
	/**
	 * Called to set a momentum to the particle e.g. in response to shockwave events
	 * @param force
	 */
	public final void setMomentum(int force)
	{
        if(force < 0) {
            throw new IllegalArgumentException("Cannot have a negative momentum: " + force);
        }
        currentMomentum.set(force);

        synchronized(momentumMutex) {
            momentumMutex.notifyAll();
        }
	}

    /**
     * Tells the particle it's allowed to move again, but obviously it will only actually move if it has
     * momentum
     */
    public void sayItCanMove() {
        move.set(true);

        synchronized(allowedToMoveMutex) {
            allowedToMoveMutex.notifyAll();
        }
    }
	
	//Processes the momentum, although only when the particle is allowed to move e.g. it may be being processed
    //so in which case it cannot move and if it has momentum
	private class MovementThread implements Runnable
	{
		public void run()
		{
			while(true)
			{
                if(!move.get()) {
                    try {
                        synchronized (allowedToMoveMutex) {
                            allowedToMoveMutex.wait();
                        }
                    }
                    catch(InterruptedException e) {
                        COMMUNICATOR.postExceptionEvent("Waiting for movement thread interrupted", e);
                    }
                }

                if(currentMomentum.get() == 0) {
                    try {
                        synchronized (momentumMutex) {
                            momentumMutex.wait();
                        }
                    }
                    catch(InterruptedException e) {
                        COMMUNICATOR.postExceptionEvent("Waiting for positive momentum thread interrupted", e);
                    }
                }


                processMomentum();
			}
		}
	}
}