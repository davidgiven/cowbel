/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.core.Method;

public class MethodParameterMismatch extends CompilationException
{
    private static final long serialVersionUID = 8952338567558812761L;
    
	private Node _node;
    private Method _method;
    
	public MethodParameterMismatch(Node node, Method method)
    {
		_node = node;
		_method = method;
    }
	
	@Override
	public String getMessage()
	{
		return "Method parameter mismatch in call to " +
				_method.getName() + " at " + _node.locationAsString();
	}
}
