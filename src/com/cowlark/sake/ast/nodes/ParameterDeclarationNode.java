package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class ParameterDeclarationNode extends ExpressionNode
{
	public ParameterDeclarationNode(Location start, Location end,
			IdentifierNode name, TypeNode type)
    {
		super(start, end);
		addChild(name);
		addChild(type);
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
