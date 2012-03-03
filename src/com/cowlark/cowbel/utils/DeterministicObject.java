/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.utils;

public class DeterministicObject<T extends DeterministicObject<T>> implements
		Comparable<T>
{
	private static int _globalid = 0;
	
	private int _id = _globalid++;
	
    public int getId()
    {
	    return _id;
    }
	
	@Override
    public int compareTo(T other)
	{
		if (_id < other._id)
			return -1;
		if (_id == other._id)
			return 0;
		return 1;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (this == obj);
	}
	
	@Override
	public String toString()
	{
	    return getClass().getSimpleName() + _id;
	}
}
