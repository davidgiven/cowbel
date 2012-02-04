/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.TypeContext;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;

public class TypeNotFound extends CompilationException
{
    private static final long serialVersionUID = -2409430033855342682L;
    
	private TypeContext _typecontext;
    private IdentifierNode _name;
    
	public TypeNotFound(TypeContext typecontext, IdentifierNode name)
    {
		_typecontext = typecontext;
		_name = name;
    }
	
	@Override
	public String getMessage()
	{
		return "Type '" + _name.getText() + "' not found in scope at " +
			_typecontext.getNode().locationAsString();
	}
}
