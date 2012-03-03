/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.InterfaceListNode;
import com.cowlark.cowbel.interfaces.IsNode;

public class TypeParameterMismatch extends CompilationException
{
    private static final long serialVersionUID = 5677501661277120026L;
    
	private IsNode _caller;
    private IsNode _definition;
    private IdentifierListNode _ids;
    private InterfaceListNode _types;
    
	public TypeParameterMismatch(IsNode caller,
			IsNode definition,
			IdentifierListNode ids,
			InterfaceListNode types)
    {
		_caller = caller;
		_definition = definition;
		_ids = ids;
		_types = types;
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
