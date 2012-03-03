/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.core.Callable;
import com.cowlark.cowbel.interfaces.IsCallNode;

public class FunctionParameterMismatch extends CompilationException
{
    private static final long serialVersionUID = 2464239362015604667L;
    
	private IsCallNode _node;
    private Callable _function; 
    
	public FunctionParameterMismatch(IsCallNode node, Callable function)
    {
		_node = node;
		_function = function;
    }
	
	@Override
	public String getMessage()
	{
		return "Function parameter mismatch in call to "+_function.toString() +
			" at " + _node.locationAsString();
	}
}
