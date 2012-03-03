/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.types.AbstractConcreteType;

public class FailedToInferTypeException extends CompilationException
{
    private static final long serialVersionUID = 1703271215661976233L;
    
    private IsNode _node;
	private AbstractConcreteType _type;
    
	public FailedToInferTypeException(IsNode node, AbstractConcreteType type)
    {
		_node = node;
		_type = type;
    }
	
	@Override
    public String getMessage()
	{
		return "Failed to infer the type of the variable at "+
			_node.locationAsString() + "; you are probably assigning it " +
			"to itself.";
	}
}
