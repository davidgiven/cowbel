package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.HasSymbol;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.symbols.Symbol;

public class VarAssignmentNode extends StatementNode implements HasSymbol
{
	private Symbol _symbol;
	
	public VarAssignmentNode(Location start, Location end,
			IdentifierNode identifier, ExpressionNode value)
    {
		super(start, end);
		addChild(identifier);
		addChild(value);
    }
	
	public IdentifierNode getVariableName()
	{
		return (IdentifierNode) getChild(0);
	}
	
	public ExpressionNode getExpression()
	{
		return (ExpressionNode) getChild(1);
	}
	
	@Override
	public String getShortDescription()
	{
		Symbol s = getSymbol();
		if (s == null)
			return getText();
		else
			return s.toString();
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
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
