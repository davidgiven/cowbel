/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import java.util.List;
import java.util.Vector;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasIdentifier;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.FunctionType;
import com.cowlark.cowbel.types.Type;

public class FunctionHeaderNode extends Node implements HasIdentifier
{
	public FunctionHeaderNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public FunctionHeaderNode(Location start, Location end,
			IdentifierNode name,
			IdentifierListNode typevariables,
			ParameterDeclarationListNode inputparams,
			ParameterDeclarationListNode outputparams)
    {
		super(start, end);
		addChild(name);
		addChild(typevariables);
		addChild(inputparams);
		addChild(outputparams);
    }	
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	public IdentifierNode getFunctionName()
	{
		return (IdentifierNode) getChild(0);
	}
	
	@Override
	public IdentifierNode getIdentifier()
	{
	    return getFunctionName();
	}
	
	public IdentifierListNode getTypeVariables()
	{
		return (IdentifierListNode) getChild(1);
	}
	
	public ParameterDeclarationListNode getInputParametersNode()
	{
		return (ParameterDeclarationListNode) getChild(2);
	}
	
	public ParameterDeclarationListNode getOutputParametersNode()
	{
		return (ParameterDeclarationListNode) getChild(3);
	}
	
	private static List<Type> parameters_to_type_list(
			ParameterDeclarationListNode node) throws CompilationException
	{
		Vector<Type> list = new Vector<Type>();
		
		for (Node n : node)
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
			AbstractTypeNode typenode = pdn.getVariableTypeNode();
			list.add(typenode.getType());
		}
		
		return list;
	}
	
	public Type calculateFunctionType() throws CompilationException
	{
		return FunctionType.create(
					parameters_to_type_list(getInputParametersNode()),
					parameters_to_type_list(getOutputParametersNode()));
	}
}
