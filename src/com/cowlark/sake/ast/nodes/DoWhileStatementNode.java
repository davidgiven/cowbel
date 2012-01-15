package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class DoWhileStatementNode extends StatementNode
{
	public DoWhileStatementNode(Location start, Location end,
			ScopeConstructorNode body,
			ExpressionNode conditional)
    {
		super(start, end);
		addChild(body);
		addChild(conditional);
    }
	
	public ScopeConstructorNode getBodyStatement()
	{
		return (ScopeConstructorNode) getChild(0);
	}
	
	public ExpressionNode getConditionalExpression()
	{
		return (ExpressionNode) getChild(1);
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
