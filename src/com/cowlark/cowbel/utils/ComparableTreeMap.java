/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public abstract class ComparableTreeMap<K extends Comparable<K>, V extends Comparable<V>>
	extends TreeMap<K, V> 
		implements Comparable<ComparableTreeMap<K, V>>
{
    private static final long serialVersionUID = 2022167358059089693L;
    
	@Override
	public int compareTo(ComparableTreeMap<K, V> o)
	{
    	if (size() < o.size())
    		return -1;
    	if (size() > o.size())
    		return 1;
    	
    	Iterator<Map.Entry<K, V>> i1 = entrySet().iterator();
    	Iterator<Map.Entry<K, V>> i2 = o.entrySet().iterator();
    	
    	while (i1.hasNext())
    	{
    		Map.Entry<K, V> e1 = i1.next();
    		Map.Entry<K, V> e2 = i2.next();
    		
    		int i = e1.getKey().compareTo(e2.getKey());
    		if (i != 0)
    			return i;
    			
    		i = e1.getValue().compareTo(e2.getValue());
    		if (i != 0)
    			return i;
    	}
    	
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
		
		return (compareTo((ComparableTreeMap<K, V>) obj) == 0);
	}
}
