/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.interfaces.IsExpressionNode;

public class InvalidFunctionCallInExpressionContext extends CompilationException
{
    private static final long serialVersionUID = 2219952398091478590L;
    
	private IsExpressionNode _node;
    
	public InvalidFunctionCallInExpressionContext(IsExpressionNode node)
    {
		_node = node;
    }
	
	@Override
	public String getMessage()
	{
		return "Function call at "+_node.locationAsString()+" must return " +
				"exactly one value to be usable in an expression context.";
	}
}
