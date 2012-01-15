package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class BooleanConstantNode extends ExpressionLiteralNode
{
	private boolean _value;
	
	public BooleanConstantNode(Location start, Location end, boolean value)
    {
        super(start, end);
        _value = value;
    }
	
	public boolean getValue()
	{
		return _value;
	}
	
	@Override
	public String getShortDescription()
	{
	    return Boolean.toString(_value);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
