/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.ExternStatementNode;

public class InvalidExternTemplate extends CompilationException
{
    private static final long serialVersionUID = 6146657938355853006L;
    
	private ExternStatementNode _node;
    
	public InvalidExternTemplate(ExternStatementNode node)
    {
		_node = node;
    }
	
	@Override
	public String getMessage()
	{
		return "The extern statement at "+_node.locationAsString()+
			" is invalid.";
	}
}
