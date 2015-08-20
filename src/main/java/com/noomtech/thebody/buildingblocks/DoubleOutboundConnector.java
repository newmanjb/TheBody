package com.noomtech.thebody.buildingblocks;

/**
 * @author Nooom, Noomtech Ltd
 */
public interface DoubleOutboundConnector 
{

	public void setForwardConnection1(Pipe leftConnection);
	
	public void setForwardConnection2(Pipe rightConnection);
}
