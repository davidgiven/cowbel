package com.cowlark.sake.symbols;

import java.util.Set;
import java.util.TreeSet;

public class GlobalSymbolStorage extends SymbolStorage
{
	private TreeSet<Function> _functions = new TreeSet<Function>();
	
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
