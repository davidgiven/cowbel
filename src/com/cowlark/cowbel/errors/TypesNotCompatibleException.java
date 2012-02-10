/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.types.Type;

public class TypesNotCompatibleException extends CompilationException
{
    private static final long serialVersionUID = 1703271215661976233L;
    
    private Node _node;
	private Type _type1;
	private Type _type2;
    
	public TypesNotCompatibleException(Node node, Type type1, Type type2)
    {
		_node = node;
		_type1 = type1;
		_type2 = type2;
    }
	
	@Override
	public String getMessage()
	{
		return "Cannot unify type " + _type2.getCanonicalTypeName() +
				" with " + _type1.getCanonicalTypeName() + " at " +
				_node.locationAsString();
	}
}
