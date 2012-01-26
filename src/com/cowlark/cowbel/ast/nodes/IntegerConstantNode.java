package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class IntegerConstantNode extends ExpressionLiteralNode
{
	private long _value;
	
	public IntegerConstantNode(Location start, Location end, long value)
    {
        super(start, end);
        _value = value;
    }
	
	public long getValue()
	{
		return _value;
	}
	
	@Override
	public String getShortDescription()
	{
		return Long.toString(_value);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}