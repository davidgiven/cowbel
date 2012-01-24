package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class WhileStatementNode extends StatementNode
{
	public WhileStatementNode(Location start, Location end,
			ExpressionNode conditional,
			ScopeConstructorNode body)
    {
		super(start, end);
		addChild(conditional);
		addChild(body);
    }
	
	public ExpressionNode getConditionalExpression()
	{
		return (ExpressionNode) getChild(0);
	}
	
	public ScopeConstructorNode getBodyStatement()
	{
		return (ScopeConstructorNode) getChild(1);
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
	
	@Override
	public boolean isLoopingNode()
	{
		return true;
	}
}
