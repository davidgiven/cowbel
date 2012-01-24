package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.HasNode;

public class MultipleDefinitionException extends CompilationException
{
	public MultipleDefinitionException(HasNode old, HasNode current)
    {
    }
}
