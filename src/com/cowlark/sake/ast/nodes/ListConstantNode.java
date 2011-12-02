package com.cowlark.sake.ast.nodes;

import java.util.List;
import com.cowlark.sake.CompilationException;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.parser.core.Location;

public class ListConstantNode extends ExpressionNode
{
	public ListConstantNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public ListConstantNode(Location start, Location end,
			List<StringConstantNode> params)
    {
		this(start, end);
		addChildren(params);
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
