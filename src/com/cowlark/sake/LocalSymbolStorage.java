package com.cowlark.sake;

import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;

public class LocalSymbolStorage extends SymbolStorage
{
	private Function _function;
	
	public LocalSymbolStorage(Function function)
    {
		_function = function;
    }
	
	public Function getDefiningFunction()
	{
		return _function;
	}
}
