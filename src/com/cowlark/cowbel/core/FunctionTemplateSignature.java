/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.interfaces.IsCallNode;

public class FunctionTemplateSignature implements Comparable<FunctionTemplateSignature>
{
	private String _name;
	private int _typeVariableCount;
	private int _inputArgumentCount;
	
	public FunctionTemplateSignature(FunctionHeaderNode node)
    {
		_name = node.getIdentifier().getText();
		_typeVariableCount = node.getTypeVariables().getNumberOfChildren();
		_inputArgumentCount = node.getInputParametersNode().getNumberOfChildren();
    }
	
	public FunctionTemplateSignature(IsCallNode node)
    {
		_name = node.getIdentifier().getText();
		_typeVariableCount = node.getTypeArguments().getNumberOfChildren();
		_inputArgumentCount = node.getInputs().getNumberOfChildren();
    }
	
	@Override
	public String toString()
	{
	    return _name + "<" + _typeVariableCount + ">(" + _inputArgumentCount + ")";
	}
	
	@Override
	public int compareTo(FunctionTemplateSignature other)
	{
		int i = _name.compareTo(other._name);
		if (i != 0)
			return i;
		
		if (_typeVariableCount < other._typeVariableCount)
			return -1;
		if (_typeVariableCount > other._typeVariableCount)
			return 1;
		
		if (_inputArgumentCount < other._inputArgumentCount)
			return -1;
		if (_inputArgumentCount > other._inputArgumentCount)
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
		
		return (compareTo((FunctionTemplateSignature) obj) == 0);
	}
}
