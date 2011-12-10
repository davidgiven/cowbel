package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.Symbol;
import com.cowlark.sake.ast.HasSymbol;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.types.Type;

public class ReturnStatementNode extends StatementNode
{
	public ReturnStatementNode(Location start, Location end,
			ExpressionNode value)
    {
		super(start, end);
		addChild(value);
    }
	
	public ExpressionNode getValue()
	{
		return (ExpressionNode) getChild(0);
	}
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
