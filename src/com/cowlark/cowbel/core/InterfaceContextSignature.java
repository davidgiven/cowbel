/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.utils.ComparableTreeMap;

public class InterfaceContextSignature extends ComparableTreeMap<TypeSignature, Interface> 
{
    private static final long serialVersionUID = 15588490203615776L;

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;

		if (obj.getClass() != getClass())
			return false;
		
		return (compareTo((InterfaceContextSignature) obj) == 0);
	}
}
