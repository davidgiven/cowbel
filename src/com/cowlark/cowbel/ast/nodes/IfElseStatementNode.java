package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class IfElseStatementNode extends StatementNode
{
	public IfElseStatementNode(Location start, Location end,
			ExpressionNode conditional,
			ScopeConstructorNode positive,
			ScopeConstructorNode negative)
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
	
	public ScopeConstructorNode getPositiveStatement()
	{
		return (ScopeConstructorNode) getChild(1);
	}
	
	public ScopeConstructorNode getNegativeStatement()
	{
		return (ScopeConstructorNode) getChild(2);
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
