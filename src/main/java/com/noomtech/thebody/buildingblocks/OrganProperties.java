package com.noomtech.thebody.buildingblocks;

/**
 * Represents a named organ with some config and some properties that are unique to that organ
 * @author Nooom, Noomtech Ltd
 */
public class OrganProperties implements Named 
{
	private String name;
	private String properties;
	
	//Only for hibernate
	private OrganProperties()
	{}
	
	public OrganProperties(String name, String properties)
	{
		this.name = name;
		this.properties = properties;
	}
	
	public final String getName()
	{
		return name;
	}
	
	private void setName(String name)
	{
		this.name = name;
	}
	
	private void setProperties(String properties)
	{
		this.properties = properties;
	}
	
	public String getProperties()
	{
		return properties;
	}
}
