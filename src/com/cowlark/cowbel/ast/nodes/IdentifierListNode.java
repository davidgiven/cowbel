/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

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
	
	IdentifierListNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public IdentifierListNode(Location start, Location end,
			List<IdentifierNode> ids)
    {
		super(start, end);
		addChildren(ids);
    }
	
	public IdentifierListNode(Location start, Location end,
			IdentifierNode... ids)
    {
		super(start, end);
		addChildren(ids);
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
		if (_symbols == null)
			_symbols = new Symbol[getNumberOfChildren()];

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
			
			if (getNumberOfChildren() > 0)
				for (Symbol s : _symbols)
					_types.add(s.getSymbolType());
		}
		return _types;
	}
			
}
