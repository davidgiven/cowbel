package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class ForStatementNode extends StatementNode
{
	public ForStatementNode(Location start, Location end,
			StatementNode initialiser,
			ExpressionNode conditional,
			StatementNode incrementer,
			StatementNode body)
    {
		super(start, end);
		addChild(initialiser);
		addChild(conditional);
		addChild(incrementer);
		addChild(body);
    }
	
	public StatementNode getInitialiserStatement()
	{
		return (StatementNode) getChild(0);
	}
	
	public ExpressionNode getConditionalExpression()
	{
		return (ExpressionNode) getChild(1);
	}
	
	public StatementNode getIncrementerStatement()
	{
		return (StatementNode) getChild(2);
	}
	
	public StatementNode getBodyStatement()
	{
		return (StatementNode) getChild(3);
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
