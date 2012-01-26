/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import java.util.List;
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
			ParameterDeclarationListNode outputparams)
    {
		super(start, end);
		addChild(name);
		addChild(inputparams);
		addChild(outputparams);
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
	
	public ParameterDeclarationListNode getOutputParametersNode()
	{
		return (ParameterDeclarationListNode) getChild(2);
	}
	
	private static List<Type> parameters_to_type_list(
			ParameterDeclarationListNode node)
	{
		Vector<Type> list = new Vector<Type>();
		
		for (Node n : node)
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
			list.add(pdn.getVariableType());
		}
		
		return list;
	}
	
	public Type getFunctionType()
	{
		if (_type == null)
		{
			_type = FunctionType.create(
					parameters_to_type_list(getParametersNode()),
					parameters_to_type_list(getOutputParametersNode()));
		}
		
		return _type;
	}
}
