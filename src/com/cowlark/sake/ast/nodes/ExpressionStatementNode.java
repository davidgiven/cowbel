package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class ExpressionStatementNode extends StatementNode
{
	public ExpressionStatementNode(Location start, Location end, ParseResult expr)
    {
        super(start, end);
        addChild((ExpressionNode) expr);
    }
	
	public ExpressionNode getExpression()
    {
	    return (ExpressionNode) getChild(0);
    }
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}