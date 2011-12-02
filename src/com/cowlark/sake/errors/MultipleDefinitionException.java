package com.cowlark.sake.errors;

import com.cowlark.sake.CompilationException;
import com.cowlark.sake.ast.nodes.IdentifierNode;

public class MultipleDefinitionException extends CompilationException
{
	public MultipleDefinitionException(IdentifierNode old,
			IdentifierNode current)
    {
    }
}
