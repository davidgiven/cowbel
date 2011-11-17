package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;

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
}