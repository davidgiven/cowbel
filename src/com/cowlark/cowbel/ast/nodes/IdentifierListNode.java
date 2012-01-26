package com.cowlark.cowbel.ast.nodes;

import java.util.ArrayList;
import java.util.List;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.types.Type;

public class IdentifierListNode extends Node
{
	private Symbol[] _symbols;
	
	public IdentifierListNode(Location start, Location end,
			List<IdentifierNode> ids)
    {
		super(start, end);
		addChildren(ids);
		
		_symbols = new Symbol[getNumberOfChildren()];
    }
	
	public IdentifierListNode(Location start, Location end)
    {
		super(start, end);
		
		_symbols = new Symbol[getNumberOfChildren()];
    }
	
	public IdentifierNode getIdentifier(int i)
	{
	    return (IdentifierNode) getChild(i);
	}
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
	
	public void setSymbol(int i, Symbol symbol)
	{
		_symbols[i] = symbol;
	}
	
	public Symbol getSymbol(int i)
	{
		return _symbols[i];
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	private List<Type> _types;
	public List<Type> calculateTypes() throws CompilationException
	{
		if (_types == null)
		{
			_types = new ArrayList<Type>();
			
			for (Symbol s : _symbols)
				_types.add(s.getSymbolType());
		}
		return _types;
	}
			
}