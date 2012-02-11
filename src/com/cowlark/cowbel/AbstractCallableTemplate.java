/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.TypeListNode;
import com.cowlark.cowbel.ast.TypeVariableNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.TypeParameterMismatch;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.types.Type;

public abstract class AbstractCallableTemplate
		implements Comparable<AbstractCallableTemplate>, HasNode
{
	private static int _globalid = 0;
	
	private int _id = _globalid++;
	private TypeContext _parentContext;
	private FunctionHeaderNode _ast;
	private String _signature;
	
	public AbstractCallableTemplate(TypeContext parentContext,
			FunctionHeaderNode ast)
	{
		_parentContext = parentContext;
		_ast = ast;
		
		StringBuilder sb = new StringBuilder();
		sb.append(_ast.getFunctionName().getText());
		sb.append("<");
		sb.append(_ast.getTypeVariables().getNumberOfChildren());
		sb.append(">(");
		sb.append(_ast.getInputParametersNode().getNumberOfChildren());
		sb.append(")");
		_signature = sb.toString();
	}
	
	@Override
    public FunctionHeaderNode getNode()
	{
		return _ast;
	}
	
	public String getSignature()
	{
		return _signature;
	}
	
	public String getName()
	{
		return _ast.getFunctionName().getText();
	}
	
	@Override
	public int compareTo(AbstractCallableTemplate other)
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
		
		IdentifierListNode ids = _ast.getTypeVariables();
		if (ids.getNumberOfChildren() != types.getNumberOfChildren())
			throw new TypeParameterMismatch(node, _ast, ids, types);
		
		/* Types in the assignment list are looked up according to the type
		 * context of the *caller*! */
		
		TypeContext tc = types.getTypeContext();
		for (int i=0; i<ids.getNumberOfChildren(); i++)
		{
			IdentifierNode id = ids.getIdentifier(i);
			TypeVariableNode typenode = types.getType(i);
			Type type = tc.lookupType(typenode);
			
			newcontext.addType(id, type);
		}
		
		return newcontext;
	}
}
