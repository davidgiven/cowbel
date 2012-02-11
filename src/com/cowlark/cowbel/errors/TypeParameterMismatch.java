/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import java.util.List;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.TypeListNode;
import com.cowlark.cowbel.types.Type;

public class TypeParameterMismatch extends CompilationException
{
    private static final long serialVersionUID = 5677501661277120026L;
    
	private Node _caller;
    private Node _definition;
    private IdentifierListNode _ids;
    private TypeListNode _types;
    
	public TypeParameterMismatch(Node caller,
			Node definition,
			IdentifierListNode ids,
			TypeListNode types)
    {
		_caller = caller;
		_definition = definition;
		_ids = ids;
		_types = types;
    }
	
	public TypeParameterMismatch(Node caller,
			Node definition,
			IdentifierListNode ids,
			List<Type> types)
    {
		_caller = caller;
		_definition = definition;
		_ids = ids;
		//_types = types;
    }
	
	@Override
	public String getMessage()
	{
		return "Function, method or type at "+_definition.locationAsString()+
			" was invoked with  an incorrect number of type parameters (" +
			_types.getNumberOfChildren() + " instead of " +
			_ids.getNumberOfChildren();
	}
}
