/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.InterfaceListNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasTypeArguments;
import com.cowlark.cowbel.utils.ById;

public class FunctionSignature implements Comparable<FunctionSignature>
{
	private FunctionTemplateSignature _templateSignature;
	private TypeAssignment _typeAssignments;
	
	public FunctionSignature(FunctionTemplateSignature templateSignature,
			AbstractCallableTemplate template, HasTypeArguments arguments)
				throws CompilationException
	{
		_templateSignature = templateSignature;
		
		_typeAssignments = new TypeAssignment();
		if (arguments != null)
		{
			FunctionHeaderNode headernode = template.getNode();
			InterfaceListNode typeparams = arguments.getTypeArguments();
			IdentifierListNode typevars = headernode.getTypeVariables();
			assert(typeparams.getNumberOfChildren() == typevars.getNumberOfChildren());
			
			for (int i = 0; i < typeparams.getNumberOfChildren(); i++)
			{
				IdentifierNode identifier = typevars.getIdentifier(i);
				Interface type = typeparams.getType(i).getInterface();
				_typeAssignments.put(new ById<IdentifierNode>(identifier), type);
			}
		}
	}

	public FunctionSignature(FunctionHeaderNode header)
	{
		_templateSignature = new FunctionTemplateSignature(header);
		_typeAssignments = new TypeAssignment();
	}
	
	public TypeAssignment getTypeAssignments()
    {
	    return _typeAssignments;
    }
	
	@Override
	public int compareTo(FunctionSignature other)
	{
		int i = _templateSignature.compareTo(other._templateSignature);
		if (i != 0)
			return i;
		
		return _typeAssignments.compareTo(other._typeAssignments); 
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;

		if (obj.getClass() != getClass())
			return false;
		
		return (compareTo((FunctionSignature) obj) == 0);
	}
}
