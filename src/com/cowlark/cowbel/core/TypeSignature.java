/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.InterfaceListNode;
import com.cowlark.cowbel.ast.TypeAssignmentNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasTypeArguments;
import com.cowlark.cowbel.utils.ById;

public class TypeSignature implements Comparable<TypeSignature>
{
	private String _name;
	private TypeAssignment _typeAssignments;
	
	public TypeSignature(InterfaceTemplate template, HasTypeArguments arguments)
			throws CompilationException
	{
		_name = template.getNode().getIdentifier().getText();
		
		_typeAssignments = new TypeAssignment();
		if (arguments != null)
		{
			TypeAssignmentNode assignmentnode = template.getNode();
			InterfaceListNode typeparams = arguments.getTypeArguments();
			IdentifierListNode typevars = assignmentnode.getTypeVariables();
			assert(typeparams.getNumberOfChildren() == typevars.getNumberOfChildren());
			
			for (int i = 0; i < typeparams.getNumberOfChildren(); i++)
			{
				IdentifierNode identifier = typevars.getIdentifier(i);
				Interface type = typeparams.getType(i).getInterface();
				_typeAssignments.put(new ById<IdentifierNode>(identifier), type);
			}
		}
	}

	public TypeSignature(IdentifierNode identifier)
	{
		_name = identifier.getText();
		_typeAssignments = new TypeAssignment();
	}
	
	public TypeSignature(String name)
	{
		_name = name;
		_typeAssignments = new TypeAssignment();
	}
	
	public TypeAssignment getTypeAssignments()
    {
	    return _typeAssignments;
    }
	
	@Override
	public int compareTo(TypeSignature other)
	{
		int i = _name.compareTo(other._name);
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
		
		return (compareTo((TypeSignature) obj) == 0);
	}
}
