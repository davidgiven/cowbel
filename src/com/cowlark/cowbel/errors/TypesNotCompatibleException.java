/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.core.Interface;
import com.cowlark.cowbel.core.TypeRef;

public class TypesNotCompatibleException extends CompilationException
{
    private static final long serialVersionUID = 1703271215661976233L;
    
    private Node _node;
	private TypeRef _typeref;
	private Interface _interf;
    
	public TypesNotCompatibleException(Node node, TypeRef typeref,
			Interface interf)
    {
		_node = node;
		_typeref = typeref;
		_interf = interf;
    }
	
	@Override
	public String getMessage()
	{
		return "Type contraint " + _interf.getNameHint() + " cannot be " +
			"satisfied at " +
				_node.locationAsString();
	}
}
