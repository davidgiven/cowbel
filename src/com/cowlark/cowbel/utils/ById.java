/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.utils;


public class ById<T extends DeterministicObject<?>>
		implements Comparable<ById<T>>
{
	private T _object;
	
	public ById(T o)
	{
		_object = o;
	};
	
	public T get()
	{
		return _object;
	}
	
	@Override
	public int compareTo(ById<T> o)
	{
		int id1 = _object.getId();
		int id2 = o._object.getId();
		
		if (id1 < id2)
			return -1;
		else if (id1 > id2)
			return 1;
		return 0;
	}
	    
	@SuppressWarnings("unchecked")
    @Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;

		if (obj.getClass() != getClass())
			return false;
		
		ById<?> other = (ById<?>) obj;
		return (_object.getId() == other._object.getId());
	}
}
