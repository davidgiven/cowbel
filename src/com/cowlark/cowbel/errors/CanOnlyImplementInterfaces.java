/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.nodes.ImplementsStatementNode;
import com.cowlark.cowbel.types.Type;

public class CanOnlyImplementInterfaces extends CompilationException
{
    private static final long serialVersionUID = -7746298766610492919L;
    
	private ImplementsStatementNode _node;
    private Type _type;
    
	public CanOnlyImplementInterfaces(ImplementsStatementNode node,
			Type type)
    {
		_node = node;
		_type = type;
    }
	
	@Override
	public String getMessage()
	{
		return "The 'implements' at "+_node.locationAsString()+" can only "+
			"take an interface type as a parameter, not "+_type.getCanonicalTypeName();
	}
}
