package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.HasSymbol;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.symbols.Symbol;

public class FunctionDefinitionNode extends StatementNode implements HasSymbol
{
	private Symbol _symbol;
	
	public FunctionDefinitionNode(Location start, Location end,
			FunctionHeaderNode header, ScopeConstructorNode body)
    {
		super(start, end);
		addChild(header);
		addChild(body);
    }	
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	public FunctionHeaderNode getFunctionHeader()
	{
		return (FunctionHeaderNode) getChild(0);
	}
	
	public ScopeConstructorNode getFunctionBody()
	{
		return (ScopeConstructorNode) getChild(1);
	}
	
	@Override
	public Symbol getSymbol()
	{
		return _symbol;
	}
	
	@Override
	public void setSymbol(Symbol symbol)
	{
		_symbol = symbol;
	}
}