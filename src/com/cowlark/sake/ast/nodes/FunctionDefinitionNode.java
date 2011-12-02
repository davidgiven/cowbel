package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.CompilationException;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.parser.core.Location;

public class FunctionDefinitionNode extends StatementNode
{
	public FunctionDefinitionNode(Location start, Location end,
			FunctionHeaderNode header, ScopeNode body)
    {
		super(start, end);
		addChild(header);
		addChild(body);
    }	
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	public FunctionHeaderNode getFunctionHeader()
	{
		return (FunctionHeaderNode) getChild(0);
	}
	
	public ScopeNode getFunctionBody()
	{
		return (ScopeNode) getChild(1);
	}
}