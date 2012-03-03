/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.utils.DeterministicObject;

public abstract class AbstractCallableTemplate
		extends DeterministicObject<AbstractCallableTemplate>
		implements HasNode
{
	private InterfaceContext _context;
	private FunctionHeaderNode _node;
	private FunctionTemplateSignature _signature;
	
	public AbstractCallableTemplate(InterfaceContext context,
			FunctionHeaderNode node)
	{
		_context = context;
		_node = node;
		
		_signature = new FunctionTemplateSignature(_node);
	}
	
	public InterfaceContext getContext()
    {
	    return _context;
    }
	
	@Override
    public FunctionHeaderNode getNode()
    {
	    return _node;
    }
	
	public FunctionTemplateSignature getSignature()
    {
	    return _signature;
    }
}
