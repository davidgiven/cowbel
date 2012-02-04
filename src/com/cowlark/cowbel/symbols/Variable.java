/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.symbols;

import com.cowlark.cowbel.Constructor;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.types.Type;

public class Variable extends Symbol
{
	private boolean _isParameter = false;
	private boolean _isOutputParameter = false;
	
	public Variable(Node node, IdentifierNode name, Type type)
	{
		super(node, name, type);
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
