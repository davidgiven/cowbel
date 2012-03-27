/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.core.TypeRef;
import com.cowlark.cowbel.interfaces.IsNode;

public class MustHaveTypeConstraints extends CompilationException
{
    private static final long serialVersionUID = 4445590645150386548L;
    
	private IsNode _node;
	private TypeRef _tr;
    
	public MustHaveTypeConstraints(IsNode node, TypeRef tr)
    {
		_node = node;
		_tr = tr;
    }
	
	@Override
	public String getMessage()
	{
		return "The symbol at "+_node.locationAsString()+" is being assigned " +
			"to from constructors with incompatible interfaces.";
	}
}
