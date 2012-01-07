package com.cowlark.sake.symbols;

import java.util.HashSet;
import java.util.Set;

public class GlobalSymbolStorage extends SymbolStorage
{
	private HashSet<Function> _functions = new HashSet<Function>();
	
	public GlobalSymbolStorage()
    {
    }
	
	@Override
	public void addSymbol(Symbol symbol)
	{
	    super.addSymbol(symbol);
	    if (symbol instanceof Function)
	    	_functions.add((Function) symbol);
	}
	
	public Set<Function> getFunctions()
    {
	    return _functions;
    }
}
