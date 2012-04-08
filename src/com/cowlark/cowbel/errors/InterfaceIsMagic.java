/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.ImplementsStatementNode;
import com.cowlark.cowbel.core.Interface;

public class InterfaceIsMagic extends CompilationException
{
    private static final long serialVersionUID = -4882107223572062748L;

    private ImplementsStatementNode _node;
    private Interface _interface;
    
	public InterfaceIsMagic(ImplementsStatementNode node, Interface interf)
    {
		_node = node;
		_interface = interf;
    }
	
	@Override
	public String getMessage()
	{
		return "The interface '" + _interface.getNameHint() + "' is magic and " +
			"cannot be implemented at " + _node.locationAsString();
	}
}
