package com.cowlark.sake.parser.errors;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.ScopeNode;
import com.cowlark.sake.errors.CompilationException;

public class IdentifierNotFound extends CompilationException
{
    private static final long serialVersionUID = -4102890031928413989L;

	public IdentifierNotFound(ScopeNode scope, IdentifierNode name)
    {
    }
}
