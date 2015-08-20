package com.noomtech.thebody.cells;

import com.noomtech.thebody.buildingblocks.Particle;


/**
 * Represents a red blood cell carries oxygen and carbon dioxide to a maximum capacity.
 * Note that there is no synchronisation around the manipulation of the capacities, as there will never be
 * situations where oxygen or co2 is being added and removed at the same time.
 * All methods are synchronized because you cannot perform any of these operations on the same blood cell at the same
 * time.
 * @author Nooom, Noomtech Ltd
 */
public class RedBloodCell extends Particle 
{
	//�configurable or dynamic?
	private static final int O2_CAPACITY = 100;
	//�configurable or dynamic?
	private static final int CO2_CAPACITY = 100;

	private int cO2Held;
	private int o2Held;
	
	
	//For hibernate
	private RedBloodCell()
	{
		super();
	}

	
	public RedBloodCell(String name)
	{
		super(name);
	}
	
	
	public synchronized int removeAllCO2()
	{
		int tempCO2 = cO2Held;
		cO2Held = 0;
		return tempCO2;
	}
	
	public synchronized int addCO2(int co2Amount)
	{	
		int amountToAdd = co2Amount;
		if(co2Amount > (CO2_CAPACITY - cO2Held))
		{			
			amountToAdd = (CO2_CAPACITY - cO2Held); 
		}
		
		cO2Held += amountToAdd;
				
		return amountToAdd;
	} 
	
	public synchronized int takeO2(int amountToTake)
	{
		if(amountToTake > o2Held)
		{
			amountToTake = o2Held; 
		}

		o2Held -= amountToTake;
		
		return amountToTake;
	}

    public synchronized int getO2ThatCanBeAdded() {
        return O2_CAPACITY - o2Held;
    }

	public synchronized int addO2(int o2Amount)
	{	
		int amountToAdd = o2Amount;
		if(o2Amount > (O2_CAPACITY - o2Held))
		{			
			amountToAdd = (O2_CAPACITY - o2Held); 
		}		
		
		o2Held += amountToAdd;
				
		return amountToAdd;
	}

    public int getO2Level() {
        return o2Held;
    }
}