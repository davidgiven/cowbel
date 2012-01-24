package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class ForStatementNode extends StatementNode
{
	public ForStatementNode(Location start, Location end,
			StatementNode initialiser,
			ExpressionNode conditional,
			StatementNode incrementer,
			ScopeConstructorNode body)
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
	
	public ScopeConstructorNode getBodyStatement()
	{
		return (ScopeConstructorNode) getChild(3);
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
