package com.noomtech.thebody.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Given that, as of 27/09/12, there is no appropriate concurrent Set implementation in Java this is my own version which simply creates a
 * Set based on ConcurrentHashMap using the Collections.newSetFromMap method.
 * @author Nooom, Noomtech Ltd
 */
public class ConcurrentHashSet <T> implements Set<T> 
{
	private Set<T> backingSet;
	
	
	public ConcurrentHashSet()
	{
		backingSet = Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());
	}
	

	public int size() 
	{
		return backingSet.size();	
	}

	public boolean isEmpty() 
	{
		return backingSet.isEmpty();		
	}

	public boolean contains(Object o) 
	{
		return backingSet.contains(o);
	}

	public Iterator<T> iterator() 
	{
		return backingSet.iterator();
	}

	public <S> S[] toArray(S[] a)
	{
		return backingSet.toArray(a);
	}

	public boolean add(T e) 
	{
		return backingSet.add(e);
	}

	public boolean remove(Object o) 
	{
		return backingSet.remove(o);
	}

	public boolean containsAll(Collection<?> c) 
	{
		return backingSet.containsAll(c);
	}

	public boolean addAll(Collection<? extends T> c) 
	{
		return backingSet.addAll(c);
	}

	public boolean retainAll(Collection<?> c) 
	{
		return backingSet.retainAll(c);
	}

	public boolean removeAll(Collection<?> c) 
	{
		return backingSet.removeAll(c);
	}

	public void clear() 
	{
		backingSet.clear();		
	}

	public Object[] toArray() 
	{	
		return backingSet.toArray();
	}
}
