package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.symbols.Symbol;

public interface HasSymbol
{
	public void setSymbol(Symbol symbol);
	public Symbol getSymbol();
}
