package com.noomtech.thebody.circulatorysystem;

import com.noomtech.thebody.buildingblocks.Named;
import com.noomtech.thebody.buildingblocks.SingleConnectorPipe;
import com.noomtech.thebody.communication.Communicator;
import com.noomtech.thebody.communication.CommunicatorFactory;

/**
 * Represents the heart (obviously).
 * @author Nooom, Noomtech Ltd
 */
public class Heart implements Named
{
	//can have several chambers, each one a pipe and connected to the other one like proper heart,
	//but just have one for now with the heart only having one outbound connection
	private final Atrium atrium1;
	private volatile boolean stopped = true;
	private int heartBeatInterval;
	//Use to build shockwaves for the heart-beats
	private volatile int heartBeatForce = 1;
	//The thread that is responsible for heart-beats
	private final Thread lifeThread;
	private static final Communicator COMMUNICATOR = CommunicatorFactory.getCommunicator();
	private static final String NAME = "Heart";
	
	
	public Heart(Atrium atrium, int heartBeatInterval, int heartBeatForce)
	{
		this.heartBeatInterval = heartBeatInterval;
		atrium1 = atrium;
		atrium1.setBeatForce(heartBeatForce);
		lifeThread = new Thread(new HeartBeatRunnable());
		lifeThread.setName(atrium1.getName() + " - heartbeater");
	}
	
	
	public void setHeartBeatForce(int hearBeatForce)
	{
		this.heartBeatForce = hearBeatForce;
	}
	
	public SingleConnectorPipe getAtrium1()
	{
		return atrium1;
	}
	
	public String getName()
	{
		return NAME;
	}
	
	public void stop()
	{
		if(stopped)
		{
			throw new IllegalArgumentException("Heart is already stopped");
		}

		lifeThread.interrupt();
	}
	
	public void start()
	{
		if(!stopped)
		{
			throw new IllegalArgumentException("Heart is already started");			
		}
				
		lifeThread.start();
		COMMUNICATOR.postEvent(this, "Heart started", "Heart.Started");
	}
	
	private class HeartBeatRunnable implements Runnable
	{
		public void run()
		{
			try
			{
				stopped = false;
				while(!stopped)
				{
					atrium1.beat();
					Thread.sleep(heartBeatInterval);					
				}
			}
			catch(InterruptedException e)
			{
				//@todo - heart attack?
			}	
			catch(Exception e)
			{
				COMMUNICATOR.postExceptionEvent(e.getMessage(), e);				
			}
			finally
			{
				COMMUNICATOR.postEvent(this, "Heart stopped", "Heart.Stopped");
				stopped = true;
			}
		}
	}	
}