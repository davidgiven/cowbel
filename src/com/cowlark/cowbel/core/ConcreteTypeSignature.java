/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import com.cowlark.cowbel.types.AbstractConcreteType;

public class ConcreteTypeSignature implements Comparable<ConcreteTypeSignature>
{
	private SortedSet<Interface> _members;
	
	public ConcreteTypeSignature(SortedSet<AbstractConcreteType> members)
	{
		_members = new TreeSet<Interface>();
		for (AbstractConcreteType ctype : members)
			_members.addAll(ctype.getSupportedInterfaces());
	}

	@Override
	public int compareTo(ConcreteTypeSignature other)
	{
		int thiscount = _members.size();
		int thatcount = other._members.size();
		int commoncount = Math.min(thiscount, thatcount);
		
		Iterator<Interface> thisi = _members.iterator();
		Iterator<Interface> thati = other._members.iterator();
		
		for (int i = 0; i < commoncount; i++)
		{
			int c = thisi.next().compareTo(thati.next());
			if (c != 0)
				return c;
		}
		
		if (thiscount < thatcount)
			return 1;
		if (thiscount > thatcount)
			return -1;
		return 0;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;

		if (obj.getClass() != getClass())
			return false;
		
		return (compareTo((ConcreteTypeSignature) obj) == 0);
	}
}
