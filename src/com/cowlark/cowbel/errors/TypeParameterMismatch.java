/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.nodes.FunctionHeaderNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.TypeListNode;

public class TypeParameterMismatch extends CompilationException
{
    private static final long serialVersionUID = 5677501661277120026L;
    
	private Node _node;
    private FunctionHeaderNode _header;
    private IdentifierListNode _ids;
    private TypeListNode _types;
    
	public TypeParameterMismatch(Node node,
			FunctionHeaderNode function,
			IdentifierListNode ids,
			TypeListNode types)
    {
		_node = node;
		_header = function;
		_ids = ids;
		_types = types;
    }
	
	@Override
	public String getMessage()
	{
		return "Function or method at "+_header.locationAsString()+" was " +
			"called with  an incorrect number of type parameters (" +
			_types.getNumberOfChildren() + " instead of " +
			_ids.getNumberOfChildren();
	}
}
