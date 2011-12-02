package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.CompilationException;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.parser.core.Location;

public class StringConstantNode extends ExpressionNode
{
	private String _value;
	
	public StringConstantNode(Location start, Location end, String value)
    {
        super(start, end);
        _value = value;
    }
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
