/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import java.util.Set;
import com.cowlark.cowbel.ast.nodes.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.symbols.Symbol;

public class AmbiguousVariableReference extends CompilationException
{
    private static final long serialVersionUID = 8172326847183615951L;
    
	private AbstractScopeConstructorNode _scope;
    private IdentifierNode _name;
    private Set<Symbol> _matches;
    
	public AmbiguousVariableReference(AbstractScopeConstructorNode scope,
			IdentifierNode name, Set<Symbol> matches)
    {
		_scope = scope;
		_name = name;
		_matches = matches;
    }
	
	@Override
	public String getMessage()
	{
		return "Identifier '" + _name.getText() + "' is ambiguous when used like this at " +
			_scope.toString();
	}
}
