/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.HasNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionHeaderNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.TypeListNode;
import com.cowlark.cowbel.ast.nodes.TypeVariableNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FunctionTypeParameterMismatch;
import com.cowlark.cowbel.types.Type;

public class FunctionTemplate implements Comparable<FunctionTemplate>, HasNode
{
	private static int _globalid = 0;
	
	private int _id = _globalid++;
	private TypeContext _parentContext;
	private FunctionDefinitionNode _ast;
	private String _signature;
	
	public FunctionTemplate(TypeContext parentContext,
			FunctionDefinitionNode ast)
	{
		_parentContext = parentContext;
		_ast = ast;
		
		FunctionHeaderNode header = _ast.getFunctionHeader();
		
		StringBuilder sb = new StringBuilder();
		sb.append(header.getFunctionName().getText());
		sb.append("<");
		sb.append(header.getTypeVariables().getNumberOfChildren());
		sb.append(">(");
		sb.append(header.getInputParametersNode().getNumberOfChildren());
		sb.append(")");
		_signature = sb.toString();
	}
	
	@Override
    public FunctionDefinitionNode getNode()
	{
		return _ast;
	}
	
	public String getSignature()
	{
		return _signature;
	}
	
	public String getName()
	{
		return _ast.getFunctionHeader().getFunctionName().getText();
	}
	
	@Override
	public int compareTo(FunctionTemplate other)
	{
		if (_id < other._id)
			return -1;
		if (_id > other._id)
			return 1;
		return 0;
	}

	public TypeContext createTypeContext(Node node, TypeListNode types)
		throws CompilationException
	{
		if ((types == null) || (types.getNumberOfChildren() == 0))
			return _parentContext;
		
		TypeContext newcontext = new TypeContext(node, _parentContext);
		
		IdentifierListNode ids = _ast.getFunctionHeader().getTypeVariables();
		if (ids.getNumberOfChildren() != types.getNumberOfChildren())
			throw new FunctionTypeParameterMismatch(node, _ast, ids, types);
		
		for (int i=0; i<ids.getNumberOfChildren(); i++)
		{
			IdentifierNode id = ids.getIdentifier(i);
			TypeVariableNode typenode = types.getType(i);
			Type type = _parentContext.lookupType(typenode.getIdentifier());
			
			newcontext.addType(id, type);
		}
		
		return newcontext;
	}
}
