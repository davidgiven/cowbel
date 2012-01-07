package com.cowlark.sake.symbols;


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
