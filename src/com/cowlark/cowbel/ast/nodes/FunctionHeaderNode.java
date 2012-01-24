package com.cowlark.cowbel.ast.nodes;

import java.util.Vector;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.FunctionType;
import com.cowlark.cowbel.types.Type;

public class FunctionHeaderNode extends Node
{
	private Type _type;
	
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
	
	public ParameterDeclarationListNode getParametersNode()
	{
		return (ParameterDeclarationListNode) getChild(1);
	}
	
	public TypeNode getReturnTypeNode()
	{
		return (TypeNode) getChild(2);
	}
	
	public Type getFunctionType()
	{
		if (_type == null)
		{
			Vector<Type> params = new Vector<Type>();
			
			ParameterDeclarationListNode pdln = getParametersNode();
			for (Node n : pdln.getChildren())
			{
				ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
				
				params.add(pdn.getVariableType());
			}
			
			_type = FunctionType.create(params, getReturnTypeNode().getType());
		}
		
		return _type;
	}
}
