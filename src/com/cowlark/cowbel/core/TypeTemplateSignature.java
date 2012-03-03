/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.TypeAssignmentNode;
import com.cowlark.cowbel.ast.TypeVariableNode;

public class TypeTemplateSignature implements Comparable<TypeTemplateSignature>
{
	private String _name;
	private int _typeVariableCount;
	
	public TypeTemplateSignature(String name)
    {
		_name = name;
		_typeVariableCount = 0;
    }
	
	public TypeTemplateSignature(TypeAssignmentNode node)
    {
		_name = node.getIdentifier().getText();
		_typeVariableCount = node.getTypeVariables().getNumberOfChildren();
    }
	
	public TypeTemplateSignature(TypeVariableNode node)
    {
		_name = node.getIdentifier().getText();
		_typeVariableCount = node.getTypeArguments().getNumberOfChildren();
    }
	
	@Override
	public String toString()
	{
	    return _name + "<" + _typeVariableCount + ">";
	}
	
	@Override
	public int compareTo(TypeTemplateSignature other)
	{
		int i = _name.compareTo(other._name);
		if (i != 0)
			return i;
		
		if (_typeVariableCount < other._typeVariableCount)
			return -1;
		if (_typeVariableCount > other._typeVariableCount)
			return 1;
		return 0;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;

		if (obj.getClass() != getClass())
			return false;
		
		return (compareTo((TypeTemplateSignature) obj) == 0);
	}
}
