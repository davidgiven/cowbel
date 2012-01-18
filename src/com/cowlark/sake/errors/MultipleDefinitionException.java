package com.cowlark.sake.errors;

import com.cowlark.sake.ast.HasNode;

public class MultipleDefinitionException extends CompilationException
{
	public MultipleDefinitionException(HasNode old, HasNode current)
    {
    }
}
