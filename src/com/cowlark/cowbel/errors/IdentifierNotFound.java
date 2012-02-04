/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.nodes.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;

public class IdentifierNotFound extends CompilationException
{
    private static final long serialVersionUID = -4102890031928413989L;

    private AbstractScopeConstructorNode _scope;
    private IdentifierNode _name;
    
	public IdentifierNotFound(AbstractScopeConstructorNode scope, IdentifierNode name)
    {
		_scope = scope;
		_name = name;
    }
	
	@Override
	public String getMessage()
	{
		return "Identifier '" + _name.getText() + "' not found at " +
			_name.locationAsString();
	}
}
