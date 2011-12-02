package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class FunctionHeaderNode extends Node
{
	public FunctionHeaderNode(Location start, Location end,
			IdentifierNode name,
			ParameterDeclarationListNode inputparams,
			TypeNode returntype)
    {
		super(start, end);
		addChild(name);
		addChild(inputparams);
		addChild(returntype);
    }	
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	public IdentifierNode getFunctionName()
	{
		return (IdentifierNode) getChild(0);
	}
}
