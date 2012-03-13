/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.symbols;

import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.core.Constructor;
import com.cowlark.cowbel.core.TypeRef;
import com.cowlark.cowbel.interfaces.IsNode;

public class Variable extends Symbol
{
	private boolean _isParameter = false;
	private boolean _isOutputParameter = false;
	
	public Variable(IsNode node, IdentifierNode name, TypeRef typeref)
	{
		super(node, name, typeref);
	}

	public boolean isParameter()
    {
	    return _isParameter;
    }
	
	public void setParameter(boolean isParameter)
    {
	    _isParameter = isParameter;
    }
	
	public boolean isOutputParameter()
    {
	    return _isOutputParameter;
    }
	
	public void setOutputParameter(boolean isOutputParameter)
    {
	    _isOutputParameter = isOutputParameter;
    }
	
	@Override
	public void addToConstructor(Constructor constructor)
	{
		constructor.addVariable(this);
	}
}
