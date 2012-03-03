/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.Node;

public class NoSuchMethodException extends CompilationException
{
    private static final long serialVersionUID = -2280542063644019979L;
    
	private Node _node;
	private IdentifierNode _id;
	
	public NoSuchMethodException(Node node, IdentifierNode id)
    {
		_node = node;
		_id = id;
    }
	
	@Override
	public String toString()
	{
		return "No such method '" + _id.getText() + "' at " +
			_node.toString();
	}
}
