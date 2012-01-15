package com.cowlark.sake.parser.errors;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.ScopeConstructorNode;
import com.cowlark.sake.errors.CompilationException;

public class IdentifierNotFound extends CompilationException
{
    private static final long serialVersionUID = -4102890031928413989L;

    private ScopeConstructorNode _scope;
    private IdentifierNode _name;
    
	public IdentifierNotFound(ScopeConstructorNode scope, IdentifierNode name)
    {
		_scope = scope;
		_name = name;
    }
	
	@Override
	public String getMessage()
	{
		return "Identifier '" + _name.getText() + "' not found in scope at " +
			_scope.toString();
	}
}
