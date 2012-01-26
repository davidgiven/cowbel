package com.cowlark.cowbel.ast.nodes;

import java.util.List;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class ParameterDeclarationListNode extends ExpressionNode
{
	public ParameterDeclarationListNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public ParameterDeclarationListNode(Location start, Location end,
			List<ParameterDeclarationNode> params)
    {
		this(start, end);
		addChildren(params);
    }
	
	public ParameterDeclarationListNode(Location start, Location end,
			ParameterDeclarationNode... params)
    {
		this(start, end);
		addChildren(params);
    }
	
	public ParameterDeclarationNode getParameter(int i)
	{
		return (ParameterDeclarationNode) getChild(i);
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
