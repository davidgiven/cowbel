package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class IfElseStatementNode extends StatementNode
{
	public IfElseStatementNode(Location start, Location end,
			ExpressionNode conditional,
			StatementNode positive,
			StatementNode negative)
    {
		super(start, end);
		addChild(conditional);
		addChild(positive);
		addChild(negative);
    }
	
	public ExpressionNode getConditionalExpression()
	{
		return (ExpressionNode) getChild(0);
	}
	
	public StatementNode getPositiveStatement()
	{
		return (StatementNode) getChild(1);
	}
	
	public StatementNode getNegativeStatement()
	{
		return (StatementNode) getChild(2);
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
