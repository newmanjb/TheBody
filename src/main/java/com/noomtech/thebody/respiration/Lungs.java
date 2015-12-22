package com.noomtech.thebody.respiration;


import java.util.concurrent.atomic.AtomicInteger;

import com.noomtech.thebody.buildingblocks.Named;
import com.noomtech.thebody.buildingblocks.transport.Particle;
import com.noomtech.thebody.buildingblocks.transport.ParticleProcessor;
import com.noomtech.thebody.buildingblocks.transport.SingleConnectorPipe;
import com.noomtech.thebody.cells.RedBloodCell;
import com.noomtech.thebody.communication.Communicator;
import com.noomtech.thebody.communication.CommunicatorFactory;


/**
 * Represents the lungs.  It contains accessors for the in and out connections, and the alveoli are implemented as 
 * pipes that sit between these connections and process the red blood cells using the enclosing Lungs instance itself as 
 * the particle processor.
 * @author Nooom, Noomtech Ltd
 */
public class Lungs implements Named, ParticleProcessor 
{
	private static final String NAME = "Lungs";
	private SingleConnectorPipe deoxygenatedBloodInConnection;
	private SingleConnectorPipe oxygenatedBloodOutConnection;
	private final SingleConnectorPipe alveoli = new SingleConnectorPipe("Alveoli", null);
	private static final Communicator COMMUNICATOR = CommunicatorFactory.getCommunicator();
	private int redBloodCellProcessingTime;
	private AtomicInteger oxygenLevel = new AtomicInteger();
	private volatile boolean breathe;
	private Thread breathingThread = new Thread(new BreathingRunnable());
	

	//For hibernate
	private Lungs()
	{
		setUpAlveoli();
	}
	
	public Lungs(
			SingleConnectorPipe deoxygenatedBloodInConnection, 
			SingleConnectorPipe oxygenatedBloodOutConnection, 
			int redBloodCellProcessingTime)
	{
		this.deoxygenatedBloodInConnection = deoxygenatedBloodInConnection;
		this.oxygenatedBloodOutConnection = oxygenatedBloodOutConnection;
		this.redBloodCellProcessingTime = redBloodCellProcessingTime;
        oxygenLevel.set(100);
		setUpAlveoli();
		connectPipesUp();
	}
	
	private void setUpAlveoli()
	{
		alveoli.addParticleProcessor(this);
	}
	
	public String getName() 
	{	
		return NAME;
	}


	private void connectPipesUp()
	{
		if(deoxygenatedBloodInConnection != null)
		{
			deoxygenatedBloodInConnection.setForwardConnection1(alveoli);
		}
		
		if(oxygenatedBloodOutConnection != null)
		{
			alveoli.setForwardConnection1(oxygenatedBloodOutConnection);
		}
	}

	public Particle processParticle(Particle particle)
	{
		if(particle instanceof RedBloodCell)
		{
			COMMUNICATOR.postEvent(this, "Red Blood Cell (" + particle.getName() + ") being processed in " +
                    "alveoli", "Lungs.RedBloodCell.StartedProcessing");
			try
			{
				Thread.sleep(redBloodCellProcessingTime);
			}
			catch(InterruptedException e)
			{
				COMMUNICATOR.postExceptionEvent("Red blood cell processing thead interrupted", e);
			}
						
			RedBloodCell redBloodCell = (RedBloodCell)particle;
			redBloodCell.removeAllCO2();
			int availableO2ForThisCell = (int)(oxygenLevel.get() * 0.01);
            int o2ThatCellCanTake = redBloodCell.getO2ThatCanBeAdded();
            int o2ThatCellIsHaving = Math.min(o2ThatCellCanTake, availableO2ForThisCell);
			oxygenLevel.addAndGet(-o2ThatCellIsHaving);
			redBloodCell.addO2(o2ThatCellIsHaving);

			COMMUNICATOR.postEvent(this, "Finished processing red blood cell (" + redBloodCell.getName() + ") in alveoli",
                    "Lungs.RedBloodCell.FinishedProcessing");
		}

		return particle;
	}
	
	public void startBreathing()
	{
		breathe = true;
		breathingThread.start();		
	}
	
	public void stopBreathing()
	{
		breathe = false;
		breathingThread.interrupt();		
	}
	
	
	//A lot of this should be managed by input from the trachea, which connects to the lungs,
	//and in the brain e.g. oxygen levels coming from breathing and time taken to breathe etc..
	//Also, this thread that stimulates the lungs like this should be represented by the diaphragm, 
	//which is what moves in order to inflate and deflate the lungs
	private class BreathingRunnable implements Runnable
	{
		public void run()
		{
			while(breathe)
			{				
				//Breathe in
				int ctr = 100;
				while (ctr > 0)
				{
                    for(int i = 0 ; i < 9; i++) {
					    oxygenLevel.incrementAndGet();
                    }

					try
					{
						Thread.sleep(20);
					}
					catch(InterruptedException e)
					{
						COMMUNICATOR.postEvent(Lungs.this, "Breathing stopped", "Lungs.BreathingStopped");
					}		
					ctr--;
				}				
				
				if(breathe)
				{
					//Breathe out
					ctr = 100;
					while (ctr > 0)
					{
                        for(int i = 0 ; i < 9; i++) {
						    oxygenLevel.decrementAndGet();
                        }

						try
						{
							Thread.sleep(20);
						}
						catch(InterruptedException e)
						{
							COMMUNICATOR.postEvent(Lungs.this, "Breathing stopped", "Lungs.BreathingStopped");
						}		
						ctr--;
					}
				}
			}
		}
	}
}