package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class WhileStatementNode extends StatementNode
{
	public WhileStatementNode(Location start, Location end,
			ExpressionNode conditional,
			StatementNode body)
    {
		super(start, end);
		addChild(conditional);
		addChild(body);
    }
	
	public ExpressionNode getConditionalExpression()
	{
		return (ExpressionNode) getChild(0);
	}
	
	public StatementNode getBodyStatement()
	{
		return (StatementNode) getChild(1);
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
